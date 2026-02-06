package io.tezos.octezconnect.core.internal.blockchain.serializer

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.core.data.AppMetadata
import io.tezos.octezconnect.core.data.BeaconError
import io.tezos.octezconnect.core.data.Network
import io.tezos.octezconnect.core.data.Permission
import kotlinx.serialization.KSerializer

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public interface DataBlockchainSerializer {
    public val network: KSerializer<Network>
    public val permission: KSerializer<Permission>
    public val appMetadata: KSerializer<AppMetadata>
    public val error: KSerializer<BeaconError>
}