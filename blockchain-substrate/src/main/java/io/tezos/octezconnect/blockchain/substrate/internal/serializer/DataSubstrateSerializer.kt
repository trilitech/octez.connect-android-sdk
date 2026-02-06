package io.tezos.octezconnect.blockchain.substrate.internal.serializer

import io.tezos.octezconnect.blockchain.substrate.data.SubstrateAppMetadata
import io.tezos.octezconnect.core.data.AppMetadata
import io.tezos.octezconnect.core.data.BeaconError
import io.tezos.octezconnect.core.data.Network
import io.tezos.octezconnect.core.data.Permission
import io.tezos.octezconnect.core.internal.blockchain.serializer.DataBlockchainSerializer
import io.tezos.octezconnect.core.internal.utils.SuperClassSerializer
import io.tezos.octezconnect.blockchain.substrate.data.SubstrateError
import io.tezos.octezconnect.blockchain.substrate.data.SubstrateNetwork
import io.tezos.octezconnect.blockchain.substrate.data.SubstratePermission
import kotlinx.serialization.KSerializer

internal class DataSubstrateSerializer : DataBlockchainSerializer {
    override val network: KSerializer<Network>
        get() = SuperClassSerializer(SubstrateNetwork.serializer())

    override val permission: KSerializer<Permission>
        get() = SuperClassSerializer(SubstratePermission.serializer())

    override val appMetadata: KSerializer<AppMetadata>
        get() = SuperClassSerializer(SubstrateAppMetadata.serializer())

    override val error: KSerializer<BeaconError>
        get() = SuperClassSerializer(SubstrateError.serializer())
}