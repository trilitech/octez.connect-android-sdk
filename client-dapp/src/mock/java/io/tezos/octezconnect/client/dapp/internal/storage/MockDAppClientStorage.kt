package io.tezos.octezconnect.client.dapp.internal.storage

import io.tezos.octezconnect.client.dapp.internal.storage.decorator.DecoratedDAppClientStorage
import io.tezos.octezconnect.client.dapp.storage.DAppClientStorage
import io.tezos.octezconnect.client.dapp.storage.ExtendedDAppClientStorage
import io.tezos.octezconnect.client.dapp.data.PairedAccount
import io.tezos.octezconnect.core.internal.BeaconConfiguration
import io.tezos.octezconnect.core.internal.storage.MockStorage
import io.tezos.octezconnect.core.scope.BeaconScope
import io.tezos.octezconnect.core.storage.Storage

public class MockDAppClientStorage : DAppClientStorage, Storage by MockStorage() {
    private var activeAccount: PairedAccount? = null
    private var activePeer: String? = null

    override suspend fun getActiveAccount(): PairedAccount? = activeAccount
    override suspend fun setActiveAccount(account: PairedAccount?) {
        this.activeAccount = account
    }

    override suspend fun getActivePeer(): String? = activePeer
    override suspend fun setActivePeer(peerId: String?) {
        this.activePeer = peerId
    }

    override fun scoped(beaconScope: BeaconScope): DAppClientStorage = this
    override fun extend(beaconConfiguration: BeaconConfiguration): ExtendedDAppClientStorage = DecoratedDAppClientStorage(this, beaconConfiguration)
}