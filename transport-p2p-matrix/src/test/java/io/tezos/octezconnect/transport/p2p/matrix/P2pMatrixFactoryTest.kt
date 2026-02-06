package io.tezos.octezconnect.transport.p2p.matrix

import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.tezos.octezconnect.core.internal.BeaconConfiguration
import io.tezos.octezconnect.core.internal.migration.Migration
import io.tezos.octezconnect.core.internal.storage.MockSecureStorage
import io.tezos.octezconnect.core.internal.storage.MockStorage
import io.tezos.octezconnect.core.internal.storage.StorageManager
import io.tezos.octezconnect.core.internal.utils.IdentifierCreator
import io.tezos.octezconnect.core.network.provider.HttpClientProvider
import io.tezos.octezconnect.core.scope.BeaconScope
import io.tezos.octezconnect.transport.p2p.matrix.internal.BeaconP2pMatrixConfiguration
import io.tezos.octezconnect.transport.p2p.matrix.internal.di.ExtendedDependencyRegistry
import io.tezos.octezconnect.transport.p2p.matrix.internal.di.P2pMatrixDependencyRegistry
import io.tezos.octezconnect.transport.p2p.matrix.internal.storage.MockP2pMatrixStoragePlugin
import io.tezos.octezconnect.transport.p2p.matrix.internal.storage.sharedpreferences.SharedPreferencesP2pMatrixStoragePlugin
import io.tezos.octezconnect.transport.p2p.matrix.storage.ExtendedP2pMatrixStoragePlugin
import mockBeaconSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

internal class P2pMatrixFactoryTest {

    @MockK(relaxed = true)
    private lateinit var migration: Migration

    @MockK
    private lateinit var identifierCreator: IdentifierCreator

    private lateinit var dependencyRegistry: ExtendedDependencyRegistry
    private lateinit var storageManager: StorageManager

    private val beaconScope: BeaconScope = BeaconScope.Global

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        dependencyRegistry = spyk(P2pMatrixDependencyRegistry(mockk(relaxed = true)))

        mockBeaconSdk(dependencyRegistry = dependencyRegistry)

        storageManager = spyk(StorageManager(beaconScope, MockStorage(), MockSecureStorage(), identifierCreator, BeaconConfiguration(ignoreUnsupportedBlockchains = false)))

        every { dependencyRegistry.storageManager } returns storageManager
        every { dependencyRegistry.migration } returns migration
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun `creates P2pMatrix instance with defaults`() {
        val factory = P2pMatrix.Factory()
        val p2pMatrix = factory.create(dependencyRegistry)

        assertTrue(storageManager.hasPlugin<ExtendedP2pMatrixStoragePlugin>(), "Expected ExtendedP2pMatrixStoragePlugin to be registered.")

        verify(exactly = 1) { storageManager.addPlugins(any<SharedPreferencesP2pMatrixStoragePlugin>()) }

        verify { dependencyRegistry.httpClient(null) }
        verify { dependencyRegistry.matrixClient(any()) }
        verify { dependencyRegistry.p2pMatrixStore(any(), BeaconP2pMatrixConfiguration.defaultNodes) }
    }

    @Test
    fun `creates P2pMatrix instance as builder function with defaults`() {
        val factory = P2pMatrix.Factory()
        val p2pMatrix = factory.create(dependencyRegistry)

        assertTrue(storageManager.hasPlugin<ExtendedP2pMatrixStoragePlugin>(), "Expected ExtendedP2pMatrixStoragePlugin to be registered.")

        verify(exactly = 1) { storageManager.addPlugins(any<SharedPreferencesP2pMatrixStoragePlugin>()) }

        verify { dependencyRegistry.httpClient(null) }
        verify { dependencyRegistry.matrixClient(any()) }
        verify { dependencyRegistry.p2pMatrixStore(any(), BeaconP2pMatrixConfiguration.defaultNodes) }
    }

    @Test
    fun `creates P2pMatrix instance with custom storage plugin`() {
        val plugin = MockP2pMatrixStoragePlugin().extend()
        val factory = P2pMatrix.Factory(storagePlugin = plugin)
        val p2pMatrix = factory.create(dependencyRegistry)

        assertTrue(storageManager.hasPlugin<ExtendedP2pMatrixStoragePlugin>(), "Expected ExtendedP2pMatrixStoragePlugin to be registered.")

        verify(exactly = 1) { storageManager.addPlugins(plugin) }

        verify { dependencyRegistry.httpClient(null) }
        verify { dependencyRegistry.matrixClient(any()) }
        verify { dependencyRegistry.p2pMatrixStore(any(), BeaconP2pMatrixConfiguration.defaultNodes) }
    }

    @Test
    fun `creates P2pMatrix instance as builder function with custom storage plugin`() {
        val plugin = MockP2pMatrixStoragePlugin().extend()
        val factory = P2pMatrix.Factory(storagePlugin = plugin)
        val p2pMatrix = factory.create(dependencyRegistry)

        assertTrue(storageManager.hasPlugin<ExtendedP2pMatrixStoragePlugin>(), "Expected ExtendedP2pMatrixStoragePlugin to be registered.")

        verify(exactly = 1) { storageManager.addPlugins(plugin) }

        verify { dependencyRegistry.httpClient(null) }
        verify { dependencyRegistry.matrixClient(any()) }
        verify { dependencyRegistry.p2pMatrixStore(any(), BeaconP2pMatrixConfiguration.defaultNodes) }
    }

    @Test
    fun `creates P2pMatrix instance with custom nodes`() {
        val nodes = listOf("node1", "node2")
        val factory = P2pMatrix.Factory(matrixNodes = nodes)
        val p2pMatrix = factory.create(dependencyRegistry)

        assertTrue(storageManager.hasPlugin<ExtendedP2pMatrixStoragePlugin>(), "Expected ExtendedP2pMatrixStoragePlugin to be registered.")

        verify { dependencyRegistry.httpClient(null) }
        verify { dependencyRegistry.matrixClient(any()) }
        verify { dependencyRegistry.p2pMatrixStore(any(), nodes) }
    }

    @Test
    fun `creates P2pMatrix instance as builder function with custom nodes`() {
        val nodes = listOf("node1", "node2")
        val factory = P2pMatrix.Factory(matrixNodes = nodes)
        val p2pMatrix = factory.create(dependencyRegistry)

        assertTrue(storageManager.hasPlugin<ExtendedP2pMatrixStoragePlugin>(), "Expected ExtendedP2pMatrixStoragePlugin to be registered.")

        verify { dependencyRegistry.httpClient(null) }
        verify { dependencyRegistry.matrixClient(any()) }
        verify { dependencyRegistry.p2pMatrixStore(any(), nodes) }
    }

    @Test
    fun `creates P2pMatrix instance with custom HttpProvider`() {
        val httpClientProvider = mockkClass(HttpClientProvider::class)
        val factory = P2pMatrix.Factory(httpClientProvider = httpClientProvider)
        val p2pMatrix = factory.create(dependencyRegistry)

        assertTrue(storageManager.hasPlugin<ExtendedP2pMatrixStoragePlugin>(), "Expected ExtendedP2pMatrixStoragePlugin to be registered.")

        verify { dependencyRegistry.httpClient(httpClientProvider) }
        verify { dependencyRegistry.matrixClient(any()) }
        verify { dependencyRegistry.p2pMatrixStore(any(), BeaconP2pMatrixConfiguration.defaultNodes) }
    }

    @Test
    fun `creates P2pMatrix instance as builder function with custom HttpProvider`() {
        val httpClientProvider = mockkClass(HttpClientProvider::class)
        val factory = P2pMatrix.Factory(httpClientProvider = httpClientProvider)
        val p2pMatrix = factory.create(dependencyRegistry)

        assertTrue(storageManager.hasPlugin<ExtendedP2pMatrixStoragePlugin>(), "Expected ExtendedP2pMatrixStoragePlugin to be registered.")

        verify { dependencyRegistry.httpClient(httpClientProvider) }
        verify { dependencyRegistry.matrixClient(any()) }
        verify { dependencyRegistry.p2pMatrixStore(any(), BeaconP2pMatrixConfiguration.defaultNodes) }
    }
}