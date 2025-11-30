/**
 * Generates a v4 UUID
 * @returns {string} v4 UUID (e.g. "bf01006f-1d6c-4faa-8680-36818b4681bc")
 */
function generateUUID() {
  var d = new Date().getTime();
  var d2 =
    (typeof performance !== "undefined" &&
      performance.now &&
      performance.now() * 1000) ||
    0;
  return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (c) {
    var r = Math.random() * 16;
    if (d > 0) {
      r = (d + r) % 16 | 0;
      d = Math.floor(d / 16);
    } else {
      r = (d2 + r) % 16 | 0;
      d2 = Math.floor(d2 / 16);
    }
    return (c === "x" ? r : (r & 0x3) | 0x8).toString(16);
  });
}

/**
 * Checks if a hook definition is for a native function.
 * @param {object} hook - Hook definition object.
 * @returns {boolean} True if the hook targets a native function.
 */
function isNativeHook(hook) {
  return hook.native === true;
}

/**
 * Checks if a hook definition is for an Objective-C method.
 * @param {object} hook - Hook definition object.
 * @returns {boolean} True if the hook targets an Objective-C method.
 */
function isObjCHook(hook) {
  return hook.native === true && hook.objClass !== undefined;
}

/**
 * Resolves the address of a native symbol for Interceptor.attach.
 * @param {object} hook - Native hook definition with symbol and optional module.
 * @returns {NativePointer|null} The address of the symbol, or null if not found.
 */
function resolveNativeSymbol(hook) {
  try {
    if (hook.module) {
      var mod = Process.getModuleByName(hook.module);
      return mod.getExportByName(hook.symbol);
    } else {
      return Module.getGlobalExportByName(hook.symbol);
    }
  } catch (e) {
    console.error("Failed to resolve native symbol '" + hook.symbol + "'" +
      (hook.module ? " in module '" + hook.module + "'" : "") + ": " + e);
    return null;
  }
}

/**
 * Resolves the implementation address of an Objective-C method.
 * @param {object} hook - ObjC hook definition with objClass and symbol (selector).
 * @returns {NativePointer|null} The implementation address, or null if not found.
 */
function resolveObjCMethod(hook) {
  try {
    if (!ObjC.available) {
      console.error("ObjC runtime is not available.");
      return null;
    }
    var cls = ObjC.classes[hook.objClass];
    if (!cls) {
      console.error("ObjC class '" + hook.objClass + "' not found.");
      return null;
    }
    var method = cls[hook.symbol];
    if (!method) {
      console.error("ObjC method '" + hook.symbol + "' not found in class '" + hook.objClass + "'.");
      return null;
    }
    return method.implementation;
  } catch (e) {
    console.error("Failed to resolve ObjC method '" + hook.symbol + "' in class '" + hook.objClass + "': " + e);
    return null;
  }
}

/**
 * Registers a native function hook using Frida's Interceptor API.
 * @param {object} hook - Native hook definition.
 * @param {string} categoryName - OWASP MAS category for identification.
 * @param {function} callback - Callback function for hook events.
 */
function registerNativeHook(hook, categoryName, callback) {
  var address = resolveNativeSymbol(hook);
  if (!address) {
    console.error("Cannot attach to native symbol '" + hook.symbol + "': address not resolved.");
    return;
  }

  var maxFrames = typeof hook.maxFrames === 'number' ? hook.maxFrames : 8;

  Interceptor.attach(address, {
    onEnter: function(args) {
      var stackTrace = [];
      try {
        var bt = Thread.backtrace(this.context, Backtracer.ACCURATE);
        for (var i = 0; i < bt.length; i++) {
          if (maxFrames !== -1 && i >= maxFrames) break;
          stackTrace.push(DebugSymbol.fromAddress(bt[i]).toString());
        }
      } catch (e) {
        stackTrace.push("<backtrace unavailable: " + e + ">");
      }

      this._mastgEvent = {
        id: generateUUID(),
        type: "native-hook",
        category: categoryName,
        time: new Date().toISOString(),
        module: hook.module || "<global>",
        symbol: hook.symbol,
        address: address.toString(),
        stackTrace: stackTrace
      };

      callback(this._mastgEvent);
    },
    onLeave: function(retval) {
      // Optionally emit a separate event or extend the onEnter event
    }
  });
}

/**
 * Registers an Objective-C method hook using Frida's Interceptor API.
 * @param {object} hook - ObjC hook definition with objClass and symbol.
 * @param {string} categoryName - OWASP MAS category for identification.
 * @param {function} callback - Callback function for hook events.
 */
function registerObjCHook(hook, categoryName, callback) {
  var address = resolveObjCMethod(hook);
  if (!address) {
    console.error("Cannot attach to ObjC method '" + hook.symbol + "' in class '" + hook.objClass + "': implementation not resolved.");
    return;
  }

  var maxFrames = typeof hook.maxFrames === 'number' ? hook.maxFrames : 8;

  Interceptor.attach(address, {
    onEnter: function(args) {
      var stackTrace = [];
      try {
        var bt = Thread.backtrace(this.context, Backtracer.ACCURATE);
        for (var i = 0; i < bt.length; i++) {
          if (maxFrames !== -1 && i >= maxFrames) break;
          stackTrace.push(DebugSymbol.fromAddress(bt[i]).toString());
        }
      } catch (e) {
        stackTrace.push("<backtrace unavailable: " + e + ">");
      }

      this._mastgEvent = {
        id: generateUUID(),
        type: "objc-hook",
        category: categoryName,
        time: new Date().toISOString(),
        class: hook.objClass,
        symbol: hook.symbol,
        address: address.toString(),
        stackTrace: stackTrace
      };

      callback(this._mastgEvent);
    },
    onLeave: function(retval) {
      // Optionally emit a separate event or extend the onEnter event
    }
  });
}

// Main execution
(function() {
  function callback(event) {
    console.log(JSON.stringify(event, null, 2));
  }

  var hooksSummary = [];
  var errors = [];

  target.hooks.forEach(function(hook) {
    if (!isNativeHook(hook)) {
      console.warn("Non-native hooks are not supported on iOS. Skipping hook.");
      errors.push("Non-native hook skipped: " + JSON.stringify(hook));
      return;
    }

    try {
      if (isObjCHook(hook)) {
        registerObjCHook(hook, target.category, callback);
        hooksSummary.push({
          type: "objc",
          class: hook.objClass,
          symbol: hook.symbol
        });
      } else {
        registerNativeHook(hook, target.category, callback);
        hooksSummary.push({
          type: "native",
          module: hook.module || "<global>",
          symbol: hook.symbol
        });
      }
    } catch (e) {
      var errMsg = "Failed to register hook: " + e;
      console.error(errMsg);
      errors.push(errMsg);
    }
  });

  // Emit summary
  var summary = {
    type: "summary",
    hooks: hooksSummary,
    totalHooks: hooksSummary.length,
    errors: errors,
    totalErrors: errors.length
  };
  console.log(JSON.stringify(summary, null, 2));
})();
