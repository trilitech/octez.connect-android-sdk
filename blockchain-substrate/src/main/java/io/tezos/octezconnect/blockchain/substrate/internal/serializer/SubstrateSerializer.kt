package io.tezos.octezconnect.blockchain.substrate.internal.serializer

import io.tezos.octezconnect.core.blockchain.Blockchain
import io.tezos.octezconnect.core.internal.blockchain.serializer.DataBlockchainSerializer
import io.tezos.octezconnect.core.internal.blockchain.serializer.V1BeaconMessageBlockchainSerializer
import io.tezos.octezconnect.core.internal.blockchain.serializer.V2BeaconMessageBlockchainSerializer
import io.tezos.octezconnect.core.internal.blockchain.serializer.V3BeaconMessageBlockchainSerializer

internal class SubstrateSerializer(
    override val data: DataBlockchainSerializer,
    override val v1: V1BeaconMessageBlockchainSerializer,
    override val v2: V2BeaconMessageBlockchainSerializer,
    override val v3: V3BeaconMessageBlockchainSerializer,
) : Blockchain.Serializer