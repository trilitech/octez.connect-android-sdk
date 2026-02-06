package io.tezos.octezconnect.client.dapp.internal.storage.plugin

import io.tezos.octezconnect.core.internal.BeaconConfiguration
import io.tezos.octezconnect.core.scope.BeaconScope

public interface ExtendedDAppClientStoragePlugin : DAppClientStoragePlugin {
    public suspend fun removeActiveAccount()
    public suspend fun removeActivePeer()

    override fun scoped(beaconScope: BeaconScope): ExtendedDAppClientStoragePlugin
    override fun extend(beaconConfiguration: BeaconConfiguration): ExtendedDAppClientStoragePlugin = this
}