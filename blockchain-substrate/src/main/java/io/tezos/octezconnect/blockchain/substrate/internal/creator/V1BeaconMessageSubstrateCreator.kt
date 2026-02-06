package io.tezos.octezconnect.blockchain.substrate.internal.creator

import io.tezos.octezconnect.core.internal.blockchain.creator.V1BeaconMessageBlockchainCreator
import io.tezos.octezconnect.core.internal.message.v1.V1BeaconMessage
import io.tezos.octezconnect.core.internal.utils.failWithUnsupportedMessageVersion
import io.tezos.octezconnect.core.message.BeaconMessage
import io.tezos.octezconnect.blockchain.substrate.Substrate

internal class V1BeaconMessageSubstrateCreator : V1BeaconMessageBlockchainCreator {
    @Throws(IllegalStateException::class)
    override fun from(senderId: String, message: BeaconMessage): Result<V1BeaconMessage> =
        runCatching { failWithUnsupportedMessageVersion("1", Substrate.IDENTIFIER) }
}