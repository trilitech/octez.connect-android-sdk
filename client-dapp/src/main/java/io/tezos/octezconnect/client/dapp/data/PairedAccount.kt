package io.tezos.octezconnect.client.dapp.data

import io.tezos.octezconnect.core.data.Account
import kotlinx.serialization.Serializable

@Serializable
public data class PairedAccount(
    public val account: Account,
    public val peerId: String,
)
