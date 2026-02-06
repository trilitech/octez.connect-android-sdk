package io.tezos.octezconnect.blockchain.substrate.internal.creator

import io.tezos.octezconnect.core.internal.blockchain.creator.V3BeaconMessageBlockchainCreator
import io.tezos.octezconnect.core.internal.message.v3.*
import io.tezos.octezconnect.core.internal.utils.failWithIllegalState
import io.tezos.octezconnect.core.message.BeaconMessage
import io.tezos.octezconnect.blockchain.substrate.internal.message.v3.BlockchainV3SubstrateRequest
import io.tezos.octezconnect.blockchain.substrate.internal.message.v3.BlockchainV3SubstrateResponse
import io.tezos.octezconnect.blockchain.substrate.internal.message.v3.PermissionV3SubstrateRequest
import io.tezos.octezconnect.blockchain.substrate.internal.message.v3.PermissionV3SubstrateResponse
import io.tezos.octezconnect.blockchain.substrate.internal.utils.failWithUnknownMessage
import io.tezos.octezconnect.blockchain.substrate.message.request.BlockchainSubstrateRequest
import io.tezos.octezconnect.blockchain.substrate.message.request.PermissionSubstrateRequest
import io.tezos.octezconnect.blockchain.substrate.message.response.BlockchainSubstrateResponse
import io.tezos.octezconnect.blockchain.substrate.message.response.PermissionSubstrateResponse

internal class V3BeaconMessageSubstrateCreator : V3BeaconMessageBlockchainCreator {
    override fun contentFrom(message: BeaconMessage): Result<V3BeaconMessage.Content> =
        runCatching {
            with(message) {
                when (this) {
                    is PermissionSubstrateRequest -> PermissionV3BeaconRequestContent(
                        blockchainIdentifier,
                        PermissionV3SubstrateRequest.from(this),
                    )
                    is BlockchainSubstrateRequest -> BlockchainV3BeaconRequestContent(
                        blockchainIdentifier,
                        accountId ?: failWithMissingAccountId(),
                        BlockchainV3SubstrateRequest.from(this),
                    )
                    is PermissionSubstrateResponse -> PermissionV3BeaconResponseContent(
                        blockchainIdentifier,
                        PermissionV3SubstrateResponse.from(this),
                    )
                    is BlockchainSubstrateResponse -> BlockchainV3BeaconResponseContent(
                        blockchainIdentifier,
                        BlockchainV3SubstrateResponse.from(this),
                    )
                    else -> failWithUnknownMessage(message)
                }
            }
        }

    private fun failWithMissingAccountId(): Nothing = failWithIllegalState("Value `accountId` is missing in Substrate v3 request.")
}