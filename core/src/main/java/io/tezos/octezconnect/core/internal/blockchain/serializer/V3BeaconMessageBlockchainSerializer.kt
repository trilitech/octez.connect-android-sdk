package io.tezos.octezconnect.core.internal.blockchain.serializer

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.core.internal.message.v3.BlockchainV3BeaconRequestContent
import io.tezos.octezconnect.core.internal.message.v3.BlockchainV3BeaconResponseContent
import io.tezos.octezconnect.core.internal.message.v3.PermissionV3BeaconRequestContent
import io.tezos.octezconnect.core.internal.message.v3.PermissionV3BeaconResponseContent
import kotlinx.serialization.KSerializer

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public interface V3BeaconMessageBlockchainSerializer {

    // -- request --

    public val permissionRequestData: KSerializer<PermissionV3BeaconRequestContent.BlockchainData>
    public val blockchainRequestData: KSerializer<BlockchainV3BeaconRequestContent.BlockchainData>

    // -- response --

    public val permissionResponseData: KSerializer<PermissionV3BeaconResponseContent.BlockchainData>
    public val blockchainResponseData: KSerializer<BlockchainV3BeaconResponseContent.BlockchainData>
}