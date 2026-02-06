package io.tezos.octezconnect.client.dapp.internal.controller.account.store

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.client.dapp.data.PairedAccount
import io.tezos.octezconnect.core.data.Peer

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public data class AccountControllerStoreState(
    val activeAccount: PairedAccount?,
    val activePeer: Peer?,
)
