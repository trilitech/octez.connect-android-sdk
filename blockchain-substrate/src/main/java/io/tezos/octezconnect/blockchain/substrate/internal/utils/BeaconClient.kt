package io.tezos.octezconnect.blockchain.substrate.internal.utils

import io.tezos.octezconnect.blockchain.substrate.data.SubstrateNetwork
import io.tezos.octezconnect.blockchain.substrate.data.SubstratePermission
import io.tezos.octezconnect.core.client.BeaconClient
import io.tezos.octezconnect.core.internal.utils.failWithAccountNetworkNotFound

internal suspend fun BeaconClient<*>.getNetworkFor(accountId: String): SubstrateNetwork {
    val permission = getPermissionsFor(accountId) as? SubstratePermission
    return permission?.account?.network ?: failWithAccountNetworkNotFound(accountId)
}