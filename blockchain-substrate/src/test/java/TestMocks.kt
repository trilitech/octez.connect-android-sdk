import android.content.Context
import io.mockk.*
import io.tezos.octezconnect.blockchain.substrate.Substrate
import io.tezos.octezconnect.blockchain.substrate.internal.creator.SubstrateCreator
import io.tezos.octezconnect.blockchain.substrate.internal.di.ExtendedDependencyRegistry
import io.tezos.octezconnect.blockchain.substrate.internal.di.extend
import io.tezos.octezconnect.blockchain.substrate.internal.serializer.SubstrateSerializer
import io.tezos.octezconnect.core.internal.BeaconSdk
import io.tezos.octezconnect.core.internal.blockchain.BlockchainRegistry
import io.tezos.octezconnect.core.internal.data.BeaconApplication
import io.tezos.octezconnect.core.internal.di.DependencyRegistry
import io.tezos.octezconnect.core.internal.utils.currentTimestamp

// -- class --

internal fun mockBeaconSdk(
    beaconId: String = "beaconId",
    app: BeaconApplication = mockkClass(BeaconApplication::class),
    dependencyRegistry: DependencyRegistry = mockk(relaxed = true),
): BeaconSdk =
    mockkClass(BeaconSdk::class).also {
        mockkObject(BeaconSdk)
        every { BeaconSdk.instance } returns it

        val contextMock = mockk<Context>(relaxed = true)

        coEvery { it.add(any(), any(), any(), any(), any(), any()) } returns Unit

        every { it.applicationContext } returns contextMock
        every { it.app(any()) } returns app
        every { it.beaconId(any()) } returns beaconId
        every { it.dependencyRegistry(any()) } returns dependencyRegistry
    }

// -- static --

internal fun mockTime(currentTimeMillis: Long = 1) {
    mockkStatic("it.airgap.beaconsdk.core.internal.utils.TimeKt")
    every { currentTimestamp() } returns currentTimeMillis
}

internal fun mockDependencyRegistry(substrate: Substrate? = null): DependencyRegistry =
    mockkClass(DependencyRegistry::class).also {
        mockkStatic("it.airgap.beaconsdk.blockchain.substrate.internal.di.ExtendedDependencyRegistryKt")
        val extendedDependencyRegistry = mockkClass(ExtendedDependencyRegistry::class)
        every { it.extend() } returns extendedDependencyRegistry

        if (substrate != null) {
            val blockchainRegistry = mockkClass(BlockchainRegistry::class)

            every { blockchainRegistry.get(any()) } returns substrate
            every { blockchainRegistry.getOrNull(any()) } returns substrate
            every { it.blockchainRegistry } returns blockchainRegistry

            every { extendedDependencyRegistry.substrateCreator } returns substrate.creator as SubstrateCreator
            every { extendedDependencyRegistry.substrateSerializer } returns substrate.serializer as SubstrateSerializer
        }

        mockBeaconSdk(dependencyRegistry = it)
    }
