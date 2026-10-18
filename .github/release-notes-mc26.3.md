CombatLimiter for Paper 26.3 and Java 25. The existing plugin version is `3.1`; the Maven artifact remains `NoMaceNoStrength-1.0.jar`.

Install `NoMaceNoStrength-1.0.jar` in the server's `plugins` directory and restart with Java 25. No additional plugin dependency is declared.

Known existing limitation: the entry point registers the item/potion listener. Command handlers and GUI click actions described in the legacy README are not implemented by this entry point. This compatibility release does not add them. The project is no longer actively supported, as stated in the README.

The release workflow builds the tagged source with `mvn clean verify` and publishes the final JAR with `SHA256SUMS`. The repository has no automated tests. In-game behavior has not been tested.
