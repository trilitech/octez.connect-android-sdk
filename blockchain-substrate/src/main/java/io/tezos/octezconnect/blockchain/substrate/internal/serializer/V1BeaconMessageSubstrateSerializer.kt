package io.tezos.octezconnect.blockchain.substrate.internal.serializer

import io.tezos.octezconnect.core.internal.blockchain.serializer.V1BeaconMessageBlockchainSerializer
import io.tezos.octezconnect.core.internal.message.v1.V1BeaconMessage
import io.tezos.octezconnect.core.internal.utils.failWithUnsupportedMessageVersion
import io.tezos.octezconnect.blockchain.substrate.Substrate
import kotlinx.serialization.KSerializer

internal class V1BeaconMessageSubstrateSerializer : V1BeaconMessageBlockchainSerializer {
    @get:Throws(IllegalArgumentException::class)
    override val message: KSerializer<V1BeaconMessage>
        get() = failWithUnsupportedMessageVersion("1", Substrate.IDENTIFIER)
}