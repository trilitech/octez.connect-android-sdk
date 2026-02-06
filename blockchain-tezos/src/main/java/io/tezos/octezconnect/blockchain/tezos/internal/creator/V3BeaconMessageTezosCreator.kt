package io.tezos.octezconnect.blockchain.tezos.internal.creator

import io.tezos.octezconnect.blockchain.tezos.internal.message.v3.*
import io.tezos.octezconnect.blockchain.tezos.internal.message.v3.PermissionV3TezosRequest
import io.tezos.octezconnect.blockchain.tezos.internal.utils.failWithUnknownMessage
import io.tezos.octezconnect.blockchain.tezos.message.request.*
import io.tezos.octezconnect.blockchain.tezos.message.response.*
import io.tezos.octezconnect.core.internal.blockchain.creator.V3BeaconMessageBlockchainCreator
import io.tezos.octezconnect.core.internal.message.v3.*
import io.tezos.octezconnect.core.internal.utils.failWithIllegalState
import io.tezos.octezconnect.core.message.BeaconMessage

internal class V3BeaconMessageTezosCreator : V3BeaconMessageBlockchainCreator {
    override fun contentFrom(message: BeaconMessage): Result<V3BeaconMessage.Content> =
        runCatching {
            with(message) {
                when (this) {
                    is PermissionTezosRequest -> PermissionV3BeaconRequestContent(
                        blockchainIdentifier,
                        PermissionV3TezosRequest.from(this),
                    )
                    is BlockchainTezosRequest -> BlockchainV3BeaconRequestContent(
                        blockchainIdentifier,
                        accountId ?: failWithMissingAccountId(),
                        BlockchainV3TezosRequest.from(this),
                    )
                    is PermissionTezosResponse -> PermissionV3BeaconResponseContent(
                        blockchainIdentifier,
                        PermissionV3TezosResponse.from(this),
                    )
                    is BlockchainTezosResponse -> BlockchainV3BeaconResponseContent(
                        blockchainIdentifier,
                        BlockchainV3TezosResponse.from(this),
                    )
                    else -> failWithUnknownMessage(message)
                }
            }
        }

    private fun failWithMissingAccountId(): Nothing = failWithIllegalState("Value `accountId` is missing in Tezos v3 request.")
}