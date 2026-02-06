package io.tezos.octezconnect.core.storage

import io.tezos.octezconnect.core.data.AppMetadata
import io.tezos.octezconnect.core.data.Maybe
import io.tezos.octezconnect.core.data.Peer
import io.tezos.octezconnect.core.data.Permission
import io.tezos.octezconnect.core.internal.BeaconConfiguration
import io.tezos.octezconnect.core.internal.storage.decorator.DecoratedStorage
import io.tezos.octezconnect.core.scope.BeaconScope

public interface Storage {

    // -- Beacon --

    public suspend fun getMaybePeers(): List<Maybe<Peer>>
    public suspend fun setPeers(p2pPeers: List<Peer>)

    public suspend fun getMaybeAppMetadata(): List<Maybe<AppMetadata>>
    public suspend fun setAppMetadata(appMetadata: List<AppMetadata>)

    public suspend fun getMaybePermissions(): List<Maybe<Permission>>
    public suspend fun setPermissions(permissions: List<Permission>)

    // -- SDK --

    public suspend fun getSdkVersion(): String?
    public suspend fun setSdkVersion(sdkVersion: String)

    public suspend fun getMigrations(): Set<String>
    public suspend fun setMigrations(migrations: Set<String>)

    public fun scoped(beaconScope: BeaconScope): Storage
    public fun extend(beaconConfiguration: BeaconConfiguration): ExtendedStorage = DecoratedStorage(this, beaconConfiguration)
}