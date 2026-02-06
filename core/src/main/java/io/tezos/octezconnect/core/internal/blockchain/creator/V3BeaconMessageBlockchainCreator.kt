package io.tezos.octezconnect.core.internal.blockchain.creator

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.core.internal.message.v3.V3BeaconMessage
import io.tezos.octezconnect.core.message.BeaconMessage

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public interface V3BeaconMessageBlockchainCreator {
    public fun contentFrom(message: BeaconMessage): Result<V3BeaconMessage.Content>
}