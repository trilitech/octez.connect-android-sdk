package io.tezos.octezconnect.core.internal

import io.tezos.octezconnect.core.internal.data.BeaconApplication
import io.tezos.octezconnect.core.internal.di.DependencyRegistry

internal class BeaconContext(val app: BeaconApplication, val dependencyRegistry: DependencyRegistry)