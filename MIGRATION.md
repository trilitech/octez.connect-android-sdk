# Migration Guide: From Beacon Android SDK to Octez Connect Android SDK

This guide will help you migrate your Android application from the Beacon Android SDK (maintained by airgap-it) to the Octez Connect Android SDK (maintained by Trilitech).

## Overview

Octez Connect Android SDK is the successor to Beacon Android SDK. Trilitech has acquired the Beacon protocol and is now maintaining it as Octez Connect. This migration involves:

1. Updating repository references
2. Changing package imports
3. Updating Matrix node configuration (automatic)

## What's Changed

### Repository and Organization
- **Old**: `com.github.airgap-it.beacon-android-sdk`
- **New**: `com.github.trilitech.octez.connect-android-sdk`

### Package Names
- **Old**: `it.airgap.beaconsdk.*`
- **New**: `io.tezos.octezconnect.*`

### Matrix Relay Nodes
The default Matrix relay nodes have been updated from papers.tech domains to octez.io domains. This change is transparent and doesn't require any action from developers.

## Step-by-Step Migration

### 1. Update Gradle Dependencies

Open your app's `build.gradle` or `build.gradle.kts` file and update the dependency declarations:

#### For Groovy (build.gradle):

**Before:**
```groovy
dependencies {
    def beaconVersion = "4.0.1"
    
    implementation "com.github.airgap-it.beacon-android-sdk:core:$beaconVersion"
    implementation "com.github.airgap-it.beacon-android-sdk:client-wallet:$beaconVersion"
    implementation "com.github.airgap-it.beacon-android-sdk:blockchain-tezos:$beaconVersion"
    implementation "com.github.airgap-it.beacon-android-sdk:transport-p2p-matrix:$beaconVersion"
}
```

**After:**
```groovy
dependencies {
    def octezConnectVersion = "4.0.1"  // or latest version
    
    implementation "com.github.trilitech.octez.connect-android-sdk:core:$octezConnectVersion"
    implementation "com.github.trilitech.octez.connect-android-sdk:client-wallet:$octezConnectVersion"
    implementation "com.github.trilitech.octez.connect-android-sdk:blockchain-tezos:$octezConnectVersion"
    implementation "com.github.trilitech.octez.connect-android-sdk:transport-p2p-matrix:$octezConnectVersion"
}
```

#### For Kotlin DSL (build.gradle.kts):

**Before:**
```kotlin
dependencies {
    val beaconVersion = "4.0.1"
    
    implementation("com.github.airgap-it.beacon-android-sdk:core:$beaconVersion")
    implementation("com.github.airgap-it.beacon-android-sdk:client-wallet:$beaconVersion")
    implementation("com.github.airgap-it.beacon-android-sdk:blockchain-tezos:$beaconVersion")
    implementation("com.github.airgap-it.beacon-android-sdk:transport-p2p-matrix:$beaconVersion")
}
```

**After:**
```kotlin
dependencies {
    val octezConnectVersion = "4.0.1"  // or latest version
    
    implementation("com.github.trilitech.octez.connect-android-sdk:core:$octezConnectVersion")
    implementation("com.github.trilitech.octez.connect-android-sdk:client-wallet:$octezConnectVersion")
    implementation("com.github.trilitech.octez.connect-android-sdk:blockchain-tezos:$octezConnectVersion")
    implementation("com.github.trilitech.octez.connect-android-sdk:transport-p2p-matrix:$octezConnectVersion")
}
```

### 2. Update Import Statements

You'll need to update all import statements in your Kotlin/Java source files.

#### Automated Approach (Recommended)

Use your IDE's find and replace feature:
- **Find**: `import it.airgap.beaconsdk`
- **Replace with**: `import io.tezos.octezconnect`
- **Scope**: Whole project

#### Manual Examples

**Before:**
```kotlin
import it.airgap.beaconsdk.blockchain.tezos.tezos
import it.airgap.beaconsdk.client.wallet.BeaconWalletClient
import it.airgap.beaconsdk.transport.p2p.matrix.p2pMatrix
import it.airgap.beaconsdk.core.data.Connection
import it.airgap.beaconsdk.blockchain.tezos.message.request.PermissionTezosRequest
import it.airgap.beaconsdk.blockchain.tezos.message.response.PermissionTezosResponse
```

**After:**
```kotlin
import io.tezos.octezconnect.blockchain.tezos.tezos
import io.tezos.octezconnect.client.wallet.BeaconWalletClient
import io.tezos.octezconnect.transport.p2p.matrix.p2pMatrix
import io.tezos.octezconnect.core.data.Connection
import io.tezos.octezconnect.blockchain.tezos.message.request.PermissionTezosRequest
import io.tezos.octezconnect.blockchain.tezos.message.response.PermissionTezosResponse
```

### 3. Sync and Rebuild

1. Sync your Gradle files: Click "Sync Now" in Android Studio
2. Clean and rebuild your project: `Build > Clean Project` then `Build > Rebuild Project`
3. Run your app to verify everything works

## What Stays the Same

The following remain unchanged:

- **Class names**: All class names remain the same (e.g., `BeaconWalletClient`, `PermissionTezosRequest`)
- **Method signatures**: All public APIs maintain the same signatures
- **Functionality**: All features work exactly as before
- **Configuration**: Client configuration syntax remains identical

### Example - No Changes Needed in Your Logic:

```kotlin
// This code works exactly the same before and after migration
// (only imports need to change)

val client = BeaconWalletClient("My App") {
    support(tezos())    
    use(p2pMatrix())
}

client.connect()
    .onEach { result ->
        when (val message = result.getOrNull()) {
            is PermissionTezosRequest -> handlePermissionRequest(message)
            is OperationTezosRequest -> handleOperationRequest(message)
            else -> {}
        }
    }
    .launchIn(scope)
```

## Verification Checklist

After migration, verify:

- [ ] All compilation errors are resolved
- [ ] App builds successfully
- [ ] Wallet pairing works
- [ ] Message sending/receiving works
- [ ] No runtime crashes related to the SDK

## Troubleshooting

### JitPack Build Issues

If you encounter issues with JitPack not finding the new repository:
1. Wait a few minutes - JitPack builds on-demand
2. Check [https://jitpack.io/#trilitech/octez.connect-android-sdk](https://jitpack.io/#trilitech/octez.connect-android-sdk) for build status
3. Ensure you're using the correct group ID: `com.github.trilitech.octez.connect-android-sdk`

### Import Not Found Errors

If you see "unresolved reference" errors after updating imports:
1. Ensure you've updated the Gradle dependencies
2. Sync Gradle files again
3. Invalidate caches and restart Android Studio: `File > Invalidate Caches / Restart`

### ProGuard/R8 Issues

The same ProGuard rules apply as before. If you had custom rules for the old package, update them:

**Before:**
```proguard
-keep class it.airgap.beaconsdk.** { *; }
```

**After:**
```proguard
-keep class io.tezos.octezconnect.** { *; }
```

## Support

- **Documentation**: [https://docs.walletbeacon.io](https://docs.walletbeacon.io)
- **Issues**: [https://github.com/trilitech/octez.connect-android-sdk/issues](https://github.com/trilitech/octez.connect-android-sdk/issues)
- **Old Repository (for reference)**: [https://github.com/airgap-it/beacon-android-sdk](https://github.com/airgap-it/beacon-android-sdk)

## Timeline

This migration should take approximately **15-30 minutes** for most projects, depending on project size.

---

**Note**: The old `beacon-android-sdk` repository will continue to be available for reference, but all new development and updates will happen in the `octez.connect-android-sdk` repository.
