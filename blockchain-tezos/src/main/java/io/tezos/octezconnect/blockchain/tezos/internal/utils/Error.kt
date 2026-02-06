package io.tezos.octezconnect.blockchain.tezos.internal.utils

import io.tezos.octezconnect.core.internal.utils.failWithIllegalArgument
import io.tezos.octezconnect.core.message.BeaconMessage

internal fun failWithUnknownMessage(message: BeaconMessage): Nothing =
    failWithIllegalArgument("Unknown Tezos message ${message::class}")
