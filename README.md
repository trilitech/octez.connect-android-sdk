# Octez Connect Android SDK

[![stable](https://img.shields.io/github/v/tag/trilitech/octez-connect-android-sdk?label=stable&sort=semver)](https://github.com/trilitech/octez-connect-android-sdk/releases)
[![latest](https://img.shields.io/github/v/tag/trilitech/octez-connect-android-sdk?color=orange&include_prereleases&label=latest)](https://github.com/trilitech/octez-connect-android-sdk/releases)
[![license](https://img.shields.io/github/license/trilitech/octez-connect-android-sdk)](https://github.com/trilitech/octez-connect-android-sdk/blob/master/LICENSE)

> Connect Wallets with dApps on Tezos

[Octez Connect](https://github.com/trilitech/octez-connect) is an implementation of the wallet interaction standard [tzip-10](https://gitlab.com/tzip/tzip/blob/master/proposals/tzip-10/tzip-10.md) which describes the connection of a dApp with a wallet.

## About

The `Octez Connect Android SDK` provides Android developers with tools useful for setting up communication between native wallets supporting Tezos and dApps that implement the Octez Connect protocol.

**Note:** This SDK is a fork of the Beacon Android SDK (formerly maintained by AirGap/Papers). If you're migrating from Beacon Android SDK, see the [Migration Guide](docs/migration-android.md).

## Installation

To add `Octez Connect Android SDK` into your project:

  1. Make sure the [JitPack](https://jitpack.io/) repository is included in your root `build.gradle` file:

  #### Groovy
  ```groovy
  allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
  }
  ```

  #### Kotlin
  ```kotlin
  allprojects {
    repositories {
      ...
      maven("https://jitpack.io")
    }
  }
  ```

  2. Add the dependencies:

  > **Important:** Because of a [known JitPack limitation](https://github.com/jitpack/jitpack.io/issues/808) with multi-module repositories containing dots (`.`) in their name, you must replace the dot with a tilde (`~`) in the repository name portion of the group ID (`octez~connect-android-sdk`).

  #### Groovy
  ```groovy
  dependencies {
    def octezConnectVersion = "x.y.z"

    // REQUIRED, core
    implementation "com.github.trilitech.octez~connect-android-sdk:core:$octezConnectVersion"

    // optional, client-dapp
    implementation "com.github.trilitech.octez~connect-android-sdk:client-dapp:$octezConnectVersion"
  
    // optional, client-wallet
    implementation "com.github.trilitech.octez~connect-android-sdk:client-wallet:$octezConnectVersion"
    // optional, client-wallet-compat
    implementation "com.github.trilitech.octez~connect-android-sdk:client-wallet-compat:$octezConnectVersion"
  
    // optional, blockchain-substrate
    implementation "com.github.trilitech.octez~connect-android-sdk:blockchain-substrate:$octezConnectVersion"
    // optional, blockchain-tezos
    implementation "com.github.trilitech.octez~connect-android-sdk:blockchain-tezos:$octezConnectVersion"
  
    // optional, transport-p2p-matrix
    implementation "com.github.trilitech.octez~connect-android-sdk:transport-p2p-matrix:$octezConnectVersion"
  
    // REQUIRED
    def jna_version = "x.y.z"
    
    implementation "net.java.dev.jna:jna:$jna_version@aar"
  }
  ```

  #### Kotlin
  ```kotlin
  dependencies {
    val octezConnectVersion = "x.y.z"
  
    // REQUIRED, core
    implementation("com.github.trilitech.octez~connect-android-sdk:core:$octezConnectVersion")

    // optional, client-dapp
    implementation("com.github.trilitech.octez~connect-android-sdk:client-dapp:$octezConnectVersion")
  
    // optional, client-wallet
    implementation("com.github.trilitech.octez~connect-android-sdk:client-wallet:$octezConnectVersion")
    // optional, client-wallet-compat
    implementation("com.github.trilitech.octez~connect-android-sdk:client-wallet-compat:$octezConnectVersion")
  
    // optional, blockchain-substrate
    implementation("com.github.trilitech.octez~connect-android-sdk:blockchain-substrate:$octezConnectVersion")
    // optional, blockchain-tezos
    implementation("com.github.trilitech.octez~connect-android-sdk:blockchain-tezos:$octezConnectVersion")
  
    // optional, transport-p2p-matrix
    implementation("com.github.trilitech.octez~connect-android-sdk:transport-p2p-matrix:$octezConnectVersion")
  
    // REQUIRED
    val jnaVersion = "x.y.z"
    
    implementation("net.java.dev.jna:jna:$jnaVersion@aar")
  }
  ```

### Proguard and R8

`Octez Connect Android SDK` internally uses various libraries that may require custom ProGuard rules. If you're using ProGuard or R8, please follow the guides listed below to make sure your app works correctly after obfuscation:

- [ProGuard rules for Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization#android)
- [ProGuard rules for LazySodium](https://github.com/terl/lazysodium-java/wiki/installation#proguard)

### Troubleshooting

See the list of known issues and how to fix them if you run into problems after adding the dependencies.

- `Native library (com/sun/jna/xxxxx/libjnidispatch.so) not found in resource path`

    Add the `"net.java.dev.jna:jna:x.y.z@aar"` dependency **and exclude the `net.java.dev.jna` group from the Octez Connect dependencies**.
  
    #### Groovy
    ```groovy
    def withoutJna = { exclude group: "net.java.dev.jna" }
    
    implementation "com.github.trilitech.octez~connect-android-sdk:core:$octezConnectVersion", withoutJna
    implementation "com.github.trilitech.octez~connect-android-sdk:client-wallet:$octezConnectVersion", withoutJna 
    ...
  
    def jna_version = "5.9.0"
    
    implementation "net.java.dev.jna:jna:$jna_version@aar"
    ```
  
    #### Kotlin
    ```kotlin
    fun ModuleDependency.excludeJna(): ModuleDependency = apply {
        exclude(group = "net.java.dev.jna")
    }
    
    implementation("com.github.trilitech.octez~connect-android-sdk:core:$octezConnectVersion") { excludeJna() }
    implementation("com.github.trilitech.octez~connect-android-sdk:client-wallet:$octezConnectVersion") { excludeJna() }
    ...
    
    val jnaVersion = "5.9.0"
  
    implementation("net.java.dev.jna:jna:$jnaVersion@aar")
    ```

## Quickstart

The snippets below show how to quickly setup a wallet listening for incoming Octez Connect messages in Kotlin with coroutines.

### Create a wallet client and listen for incoming requests

```kotlin
import io.tezos.octezconnect.blockchain.substrate.substrate
import io.tezos.octezconnect.blockchain.tezos.tezos
import io.tezos.octezconnect.client.wallet.BeaconWalletClient
import io.tezos.octezconnect.transport.p2p.matrix.p2pMatrix

class MainActivity : AppCompatActivity() {
  lateinit var client: BeaconWalletClient

  // ...

  suspend fun listenForMessages() {
    // create a wallet client that can listen for Substrate and Tezos messages via Matrix network 
    client = BeaconWalletClient("My App") {
        support(substrate(), tezos())    
        use(p2pMatrix())
    }

    myCoroutineScope.launch {
      // subscribe to a message Flow
      client.connect().collect { /* process messages */ }
    }
  }
}
```

For more examples or examples of how to use the SDK without coroutines or in Java, please see the `demo` app.

## Migration from Beacon Android SDK

If you're migrating from the Beacon Android SDK (maintained by AirGap/Papers), please see the [Migration Guide](docs/migration-android.md) for detailed instructions on updating dependencies, imports, and configuration.

## Documentation

For detailed documentation, see the [Octez Connect documentation](https://github.com/trilitech/octez-connect).

## Project Overview

The project consists of the following modules:

### Core

Core modules are the basis for other modules. They are required for the SDK to work as expected.

| Module  | Description            | Dependencies | Required by                                                                                                                                          |
| ------- | ---------------------- | ------------ | ---------------------------------------------------------------------------------------------------------------------------------------------------- |
| `:core` | Base for other modules | ✖️           | `:client-wallet` <br /> `:client-wallet-compat` <br /><br /> `:blockchain-substrate` <br /> `:blockchain-tezos` <br /><br /> `:transport-p2p-matrix` |

### Client

Client modules ship with Octez Connect implementations for different parts of the network.

| Module                  | Description                                                                                                                                | Dependencies                    | Required by             |
| ----------------------- | ------------------------------------------------------------------------------------------------------------------------------------------ | ------------------------------- | ----------------------- |
| `:client-dapp`          | Octez Connect implementation for dApps                                                                                                            | `:core`                         | ✖️                      |
| `:client-wallet`        | Octez Connect implementation for wallets                                                                                                          | `:core`                         | `:client-wallet-compat` |
| `:client-wallet-compat` | Provides a supplementary interface for `:client-wallet` for use without [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) | `:core` <br /> `:client-wallet` | ✖️                      |

### Blockchain

Blockchain modules provide support for different blockchains.

| Module                  | Description                   | Dependencies | Required by |
| ----------------------- | ----------------------------- | ------------ | ----------- |
| `:blockchain-substrate` | Substrate specific components | `:core`      | ✖️          |
| `:blockchain-tezos`     | Tezos specific components     | `:core`      | ✖️          |

### Transport

Transport modules provide various interfaces used to establish connection between Octez Connect clients.

| Module                  | Description                                                                              | Dependencies | Required by |
| ----------------------- | ---------------------------------------------------------------------------------------- | ------------ | ----------- |
| `:transport-p2p-matrix` | Octez Connect P2P implementation which uses [Matrix](https://matrix.org/) for the communication | `:core`      | ✖️          |

### Demo

Demo modules provide examples of how to use the library. 

| Module  | Description         |
| ------- | ------------------- |
| `:demo` | Example application |

## Examples

The snippets below show how to quickly setup a wallet listening for incoming Octez Connect messages in Kotlin with coroutines. 

For more examples or examples of how to use the SDK without coroutines or in Java, please see our `demo` app.

### Create a wallet client and listen for incoming requests

```kotlin
import io.tezos.octezconnect.blockchain.substrate.substrate
import io.tezos.octezconnect.blockchain.tezos.tezos
import io.tezos.octezconnect.client.wallet.BeaconWalletClient
import io.tezos.octezconnect.transport.p2p.matrix.p2pMatrix

class MainActivity : AppCompatActivity() {
  lateinit var client: BeaconWalletClient

  // ...

  suspend fun listenForMessages() {
    // create a wallet client that can listen for Substrate and Tezos messages via Matrix network 
    client = BeaconWalletClient("My App") {
        support(substrate(), tezos())    
        use(p2pMatrix())
    }

    myCoroutineScope.launch {
      // subscribe to a message Flow
      client.connect().collect { /* process messages */ }
    }
  }
}
```

## Development

The project is built with [Gradle](https://gradle.org/).

### Product Flavors
There are the following product flavors configured:

- `prod` - the production ready version
- `mock` - provides mock implementations, used only for unit tests

### Build

#### Android Studio

Before building the project in Android Studio make sure the active build variant uses the `prod` flavor.

#### Command Line

Build all modules:
```
$ ./gradlew assembleProd
```

Build a single module:
```
$ ./gradlew :${module}:assembleProd
```

### Run Tests

#### Android Studio

Before running the tests in Android Studio make sure the active build variant uses the `mock` flavor.

#### Command Line

```
$ ./gradlew testMock{Release|Debug}UnitTest
```

---
## Related Projects

### Octez Connect Projects

[Octez Connect Web SDK](https://github.com/trilitech/octez-connect-sdk) - an SDK for web developers

### Community Projects

[Beacon Flutter SDK](https://github.com/TalaoDAO/beacon) - an SDK for Flutter developers (community maintained)
