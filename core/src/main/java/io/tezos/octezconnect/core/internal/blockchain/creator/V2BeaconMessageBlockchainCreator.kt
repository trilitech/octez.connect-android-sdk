package io.tezos.octezconnect.core.internal.blockchain.creator

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.core.internal.message.v2.V2BeaconMessage
import io.tezos.octezconnect.core.message.BeaconMessage

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public interface V2BeaconMessageBlockchainCreator {
    public fun from(senderId: String, message: BeaconMessage): Result<V2BeaconMessage>
}