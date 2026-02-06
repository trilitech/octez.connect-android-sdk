package io.tezos.octezconnect.client.dapp.internal.storage

import io.tezos.octezconnect.client.dapp.data.PairedAccount
import io.tezos.octezconnect.core.internal.storage.StorageManager
import io.tezos.octezconnect.core.internal.utils.failWithIllegalState
import io.tezos.octezconnect.client.dapp.internal.storage.plugin.DAppClientStoragePlugin
import io.tezos.octezconnect.client.dapp.internal.storage.plugin.ExtendedDAppClientStoragePlugin

internal val StorageManager.dAppClientPlugin: ExtendedDAppClientStoragePlugin
    get() = getPlugin() ?: getPlugin<DAppClientStoragePlugin>()?.extend(beaconConfiguration)?.also { addPlugins(it) } ?: failWithDAppStoragePluginMissing()

internal suspend fun StorageManager.getActiveAccount(): PairedAccount? = dAppClientPlugin.getActiveAccount()
internal suspend fun StorageManager.setActiveAccount(account: PairedAccount?) = dAppClientPlugin.setActiveAccount(account)

internal suspend fun StorageManager.removeActiveAccount() = dAppClientPlugin.removeActiveAccount()

internal suspend fun StorageManager.getActivePeer(): String? = dAppClientPlugin.getActivePeer()
internal suspend fun StorageManager.setActivePeer(publicKey: String?) = dAppClientPlugin.setActivePeer(publicKey)

internal suspend fun StorageManager.removeActivePeer() = dAppClientPlugin.removeActivePeer()

private fun failWithDAppStoragePluginMissing(): Nothing = failWithIllegalState("DApp storage plugin has not been registered.")