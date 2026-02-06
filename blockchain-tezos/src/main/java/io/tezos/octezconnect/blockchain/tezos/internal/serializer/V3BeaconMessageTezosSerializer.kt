package io.tezos.octezconnect.blockchain.tezos.internal.serializer

import io.tezos.octezconnect.blockchain.tezos.internal.message.v3.BlockchainV3TezosRequest
import io.tezos.octezconnect.blockchain.tezos.internal.message.v3.BlockchainV3TezosResponse
import io.tezos.octezconnect.blockchain.tezos.internal.message.v3.PermissionV3TezosRequest
import io.tezos.octezconnect.blockchain.tezos.internal.message.v3.PermissionV3TezosResponse
import io.tezos.octezconnect.core.internal.blockchain.serializer.V3BeaconMessageBlockchainSerializer
import io.tezos.octezconnect.core.internal.message.v3.BlockchainV3BeaconRequestContent
import io.tezos.octezconnect.core.internal.message.v3.BlockchainV3BeaconResponseContent
import io.tezos.octezconnect.core.internal.message.v3.PermissionV3BeaconRequestContent
import io.tezos.octezconnect.core.internal.message.v3.PermissionV3BeaconResponseContent
import io.tezos.octezconnect.core.internal.utils.SuperClassSerializer
import kotlinx.serialization.KSerializer

internal class V3BeaconMessageTezosSerializer : V3BeaconMessageBlockchainSerializer {
    override val permissionRequestData: KSerializer<PermissionV3BeaconRequestContent.BlockchainData>
        get() = SuperClassSerializer(PermissionV3TezosRequest.serializer())

    override val blockchainRequestData: KSerializer<BlockchainV3BeaconRequestContent.BlockchainData>
        get() = SuperClassSerializer(BlockchainV3TezosRequest.serializer())

    override val permissionResponseData: KSerializer<PermissionV3BeaconResponseContent.BlockchainData>
        get() = SuperClassSerializer(PermissionV3TezosResponse.serializer())

    override val blockchainResponseData: KSerializer<BlockchainV3BeaconResponseContent.BlockchainData>
        get() = SuperClassSerializer(BlockchainV3TezosResponse.serializer())
}