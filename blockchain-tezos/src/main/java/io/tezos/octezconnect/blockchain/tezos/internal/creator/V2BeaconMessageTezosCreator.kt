package io.tezos.octezconnect.blockchain.tezos.internal.creator

import io.tezos.octezconnect.blockchain.tezos.internal.message.v2.V2TezosMessage
import io.tezos.octezconnect.core.internal.blockchain.creator.V2BeaconMessageBlockchainCreator
import io.tezos.octezconnect.core.internal.message.v2.V2BeaconMessage
import io.tezos.octezconnect.core.message.BeaconMessage

internal class V2BeaconMessageTezosCreator : V2BeaconMessageBlockchainCreator {
    override fun from(senderId: String, message: BeaconMessage): Result<V2BeaconMessage> = runCatching { V2TezosMessage.from(senderId, message) }
}