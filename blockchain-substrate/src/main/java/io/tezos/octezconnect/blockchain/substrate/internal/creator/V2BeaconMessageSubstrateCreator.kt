package io.tezos.octezconnect.blockchain.substrate.internal.creator

import io.tezos.octezconnect.core.internal.blockchain.creator.V2BeaconMessageBlockchainCreator
import io.tezos.octezconnect.core.internal.message.v2.V2BeaconMessage
import io.tezos.octezconnect.core.internal.utils.failWithUnsupportedMessageVersion
import io.tezos.octezconnect.core.message.BeaconMessage
import io.tezos.octezconnect.blockchain.substrate.Substrate

internal class V2BeaconMessageSubstrateCreator : V2BeaconMessageBlockchainCreator {
    @Throws(IllegalStateException::class)
    override fun from(senderId: String, message: BeaconMessage): Result<V2BeaconMessage> =
        runCatching { failWithUnsupportedMessageVersion("2", Substrate.IDENTIFIER) }
}