package io.tezos.octezconnect.blockchain.tezos.internal.creator

import io.tezos.octezconnect.blockchain.tezos.internal.message.v1.V1TezosMessage
import io.tezos.octezconnect.core.internal.blockchain.creator.V1BeaconMessageBlockchainCreator
import io.tezos.octezconnect.core.internal.message.v1.V1BeaconMessage
import io.tezos.octezconnect.core.message.BeaconMessage

internal class V1BeaconMessageTezosCreator : V1BeaconMessageBlockchainCreator {
    override fun from(senderId: String, message: BeaconMessage): Result<V1BeaconMessage> = runCatching { V1TezosMessage.from(senderId, message) }
}