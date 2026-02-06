package io.tezos.octezconnect.core.internal.blockchain.creator

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.core.data.Account
import io.tezos.octezconnect.core.data.Connection
import io.tezos.octezconnect.core.data.Permission
import io.tezos.octezconnect.core.message.PermissionBeaconRequest
import io.tezos.octezconnect.core.message.PermissionBeaconResponse

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public interface DataBlockchainCreator {
    public suspend fun extractIncomingPermission(request: PermissionBeaconRequest, response: PermissionBeaconResponse, origin: Connection.Id): Result<List<Permission>>
    public suspend fun extractOutgoingPermission(request: PermissionBeaconRequest, response: PermissionBeaconResponse): Result<List<Permission>>

    public fun extractAccounts(response: PermissionBeaconResponse): Result<List<Account>>
}