package io.tezos.octezconnect.blockchain.substrate.internal.serializer

import io.tezos.octezconnect.core.internal.blockchain.serializer.V3BeaconMessageBlockchainSerializer
import io.tezos.octezconnect.core.internal.message.v3.BlockchainV3BeaconRequestContent
import io.tezos.octezconnect.core.internal.message.v3.BlockchainV3BeaconResponseContent
import io.tezos.octezconnect.core.internal.message.v3.PermissionV3BeaconRequestContent
import io.tezos.octezconnect.core.internal.message.v3.PermissionV3BeaconResponseContent
import io.tezos.octezconnect.core.internal.utils.SuperClassSerializer
import io.tezos.octezconnect.blockchain.substrate.internal.message.v3.BlockchainV3SubstrateRequest
import io.tezos.octezconnect.blockchain.substrate.internal.message.v3.BlockchainV3SubstrateResponse
import io.tezos.octezconnect.blockchain.substrate.internal.message.v3.PermissionV3SubstrateRequest
import io.tezos.octezconnect.blockchain.substrate.internal.message.v3.PermissionV3SubstrateResponse
import kotlinx.serialization.KSerializer

internal class V3BeaconMessageSubstrateSerializer : V3BeaconMessageBlockchainSerializer {
    override val permissionRequestData: KSerializer<PermissionV3BeaconRequestContent.BlockchainData>
        get() = SuperClassSerializer(PermissionV3SubstrateRequest.serializer())

    override val blockchainRequestData: KSerializer<BlockchainV3BeaconRequestContent.BlockchainData>
        get() = SuperClassSerializer(BlockchainV3SubstrateRequest.serializer())

    override val permissionResponseData: KSerializer<PermissionV3BeaconResponseContent.BlockchainData>
        get() = SuperClassSerializer(PermissionV3SubstrateResponse.serializer())

    override val blockchainResponseData: KSerializer<BlockchainV3BeaconResponseContent.BlockchainData>
        get() = SuperClassSerializer(BlockchainV3SubstrateResponse.serializer())
}