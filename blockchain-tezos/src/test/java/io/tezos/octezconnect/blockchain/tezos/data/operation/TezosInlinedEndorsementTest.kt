package io.tezos.octezconnect.blockchain.tezos.data.operation

import fromValues
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.tezos.octezconnect.blockchain.tezos.Tezos
import io.tezos.octezconnect.blockchain.tezos.internal.creator.*
import io.tezos.octezconnect.blockchain.tezos.internal.serializer.*
import io.tezos.octezconnect.blockchain.tezos.internal.wallet.TezosWallet
import io.tezos.octezconnect.core.internal.BeaconConfiguration
import io.tezos.octezconnect.core.internal.compat.CoreCompat
import io.tezos.octezconnect.core.internal.di.DependencyRegistry
import io.tezos.octezconnect.core.internal.serializer.coreJson
import io.tezos.octezconnect.core.internal.storage.MockSecureStorage
import io.tezos.octezconnect.core.internal.storage.MockStorage
import io.tezos.octezconnect.core.internal.storage.StorageManager
import io.tezos.octezconnect.core.internal.utils.IdentifierCreator
import io.tezos.octezconnect.core.scope.BeaconScope
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import mockDependencyRegistry
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class TezosInlinedEndorsementTest {

    @MockK
    private lateinit var wallet: TezosWallet

    @MockK
    private lateinit var identifierCreator: IdentifierCreator

    private lateinit var dependencyRegistry: DependencyRegistry
    private lateinit var storageManager: StorageManager

    private lateinit var json: Json

    private val beaconScope: BeaconScope = BeaconScope.Global

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        storageManager = StorageManager(beaconScope, MockStorage(), MockSecureStorage(), identifierCreator, BeaconConfiguration(ignoreUnsupportedBlockchains = false))
        val tezos = Tezos(
            wallet,
            TezosCreator(
                DataTezosCreator(storageManager, identifierCreator),
                V1BeaconMessageTezosCreator(),
                V2BeaconMessageTezosCreator(),
                V3BeaconMessageTezosCreator(),
            ),
            TezosSerializer(
                DataTezosSerializer(),
                V1BeaconMessageTezosSerializer(),
                V2BeaconMessageTezosSerializer(),
                V3BeaconMessageTezosSerializer(),
            ),
        )

        dependencyRegistry = mockDependencyRegistry(tezos)
        json = coreJson(dependencyRegistry.blockchainRegistry, CoreCompat(beaconScope))
    }

    @Test
    fun `is deserialized from JSON`() {
        listOf(
            expectedWithJson(),
            expectedWithJson(includeNulls = true),
            expectedWithJson(signature = "signature")
        )
            .map { json.decodeFromString<TezosInlinedEndorsement>(it.second) to it.first }
            .forEach {
                assertEquals(it.second, it.first)
            }
    }

    @Test
    fun `serializes to JSON`() {
        listOf(expectedWithJson())
            .map {
                json.decodeFromString(JsonObject.serializer(), json.encodeToString(it.first)) to
                        json.decodeFromString(JsonObject.serializer(), it.second)
            }
            .forEach {
                assertEquals(it.second, it.first)
            }
    }

    private fun expectedWithJson(
        branch: String = "branch",
        operations: TezosEndorsementOperation = TezosEndorsementOperation("level"),
        signature: String? = null,
        includeNulls: Boolean = false,
    ): Pair<TezosInlinedEndorsement, String> {
        val values = mapOf(
            "branch" to branch,
            "operations" to Json.encodeToJsonElement(operations),
            "signature" to signature,
        )

        val jsonObject = JsonObject.fromValues(values, includeNulls).toString()

        return TezosInlinedEndorsement(
            branch,
            operations,
            signature,
        ) to jsonObject
    }
}