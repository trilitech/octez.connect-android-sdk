package io.tezos.octezconnect.blockchain.substrate.internal.utils

import io.tezos.octezconnect.core.internal.utils.failWithIllegalArgument
import io.tezos.octezconnect.core.message.BeaconMessage

internal fun failWithUnknownMessage(message: BeaconMessage): Nothing =
    failWithIllegalArgument("Unknown Substrate message ${message::class}")