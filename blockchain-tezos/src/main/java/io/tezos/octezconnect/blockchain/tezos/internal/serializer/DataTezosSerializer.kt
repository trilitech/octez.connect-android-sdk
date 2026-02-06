package io.tezos.octezconnect.blockchain.tezos.internal.serializer

import io.tezos.octezconnect.blockchain.tezos.data.TezosAppMetadata
import io.tezos.octezconnect.blockchain.tezos.data.TezosError
import io.tezos.octezconnect.blockchain.tezos.data.TezosNetwork
import io.tezos.octezconnect.blockchain.tezos.data.TezosPermission
import io.tezos.octezconnect.core.data.AppMetadata
import io.tezos.octezconnect.core.data.BeaconError
import io.tezos.octezconnect.core.data.Network
import io.tezos.octezconnect.core.data.Permission
import io.tezos.octezconnect.core.internal.blockchain.serializer.DataBlockchainSerializer
import io.tezos.octezconnect.core.internal.utils.SuperClassSerializer
import kotlinx.serialization.KSerializer

internal class DataTezosSerializer : DataBlockchainSerializer {
    override val network: KSerializer<Network>
        get() = SuperClassSerializer(TezosNetwork.serializer())

    override val permission: KSerializer<Permission>
        get() = SuperClassSerializer(TezosPermission.serializer())

    override val appMetadata: KSerializer<AppMetadata>
        get() = SuperClassSerializer(TezosAppMetadata.serializer())

    override val error: KSerializer<BeaconError>
        get() = SuperClassSerializer(TezosError.serializer())
}