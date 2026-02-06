package io.tezos.octezconnect.blockchain.tezos.internal.utils

import io.tezos.octezconnect.blockchain.tezos.data.TezosNetwork
import io.tezos.octezconnect.blockchain.tezos.data.TezosPermission
import io.tezos.octezconnect.core.client.BeaconClient
import io.tezos.octezconnect.core.internal.utils.failWithAccountNetworkNotFound

internal suspend fun BeaconClient<*>.getNetworkFor(accountId: String): TezosNetwork {
    val permission = getPermissionsFor(accountId) as? TezosPermission ?: failWithAccountNetworkNotFound(accountId)
    return permission.network
}