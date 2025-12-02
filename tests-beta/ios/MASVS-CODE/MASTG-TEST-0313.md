---
title: Insecure Object Deserialization
platform: ios
id: MASTG-TEST-0313
type: [static, dynamic]
weakness: MASWE-0006
profiles: [L1, L2]
---

## Overview

iOS apps commonly use various object persistence mechanisms to store and retrieve data. These include `NSCoding`/`NSSecureCoding`, `Codable`, JSON encoding, property lists, XML parsing, and Object-Relational Mapping (ORM) frameworks like Core Data and Realm (see @MASTG-KNOW-0075 for details).

Insecure object persistence can lead to:

- **Exposure of sensitive data**: If serialized data is not encrypted, attackers with access to the device or backups can read sensitive information.
- **Data tampering**: Without integrity verification (such as HMAC or digital signatures), attackers can modify serialized data to bypass security controls or inject malicious payloads.
- **Insecure deserialization attacks**: If the app does not properly validate deserialized data before use, attackers can exploit this to execute arbitrary code, bypass authentication, or manipulate business logic.

When using object persistence, developers should:

- Use `NSSecureCoding` instead of `NSCoding` to ensure type safety during deserialization.
- Encrypt sensitive data before persistence and verify its integrity before use.
- Store encryption keys securely in the Keychain with appropriate protection attributes.
- Validate all deserialized data before use.

## Steps

1. Identify object persistence mechanisms used by the app using @MASTG-TECH-0058 and look for uses of serialization APIs such as `NSCoding`, `NSSecureCoding`, `Codable`, `JSONEncoder`/`JSONDecoder`, `PropertyListEncoder`/`PropertyListDecoder`, `NSKeyedArchiver`/`NSKeyedUnarchiver`, Core Data, or Realm.
2. Determine if sensitive data is being serialized by analyzing the data types being persisted.
3. For dynamic analysis, use @MASTG-TECH-0095 to hook serialization and deserialization methods and observe the data being processed.

## Observation

The output should include a list of:

- Serialization APIs being used by the app.
- Data types being serialized, including whether they contain sensitive information.
- Whether `NSSecureCoding` is used instead of `NSCoding`.
- Whether encryption and integrity verification are applied to serialized data.

## Evaluation

The test case fails if any of the following conditions are met:

- Sensitive data is serialized without encryption.
- The app uses `NSCoding` instead of `NSSecureCoding` for untrusted data.
- Serialized data lacks integrity verification (HMAC or signatures) when integrity is required.
- The app does not validate deserialized data before using it in security-relevant operations.
