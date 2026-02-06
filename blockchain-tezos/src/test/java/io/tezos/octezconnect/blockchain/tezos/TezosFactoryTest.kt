package io.tezos.octezconnect.blockchain.tezos

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.tezos.octezconnect.blockchain.tezos.internal.di.TezosDependencyRegistry
import io.tezos.octezconnect.core.internal.di.DependencyRegistry
import org.junit.Before
import org.junit.Test
import kotlin.reflect.KClass

internal class TezosFactoryTest {
    @MockK(relaxed = true)
    private lateinit var dependencyRegistry: DependencyRegistry

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { dependencyRegistry.findExtended(any<KClass<TezosDependencyRegistry>>()) } returns null
    }

    @Test
    fun `creates Tezos instance`() {
        val factory = Tezos.Factory()
        val tezos = factory.create(dependencyRegistry)
    }

    @Test
    fun `creates Tezos instance as builder function`() {
        val factory = tezos()
        val tezos = factory.create(dependencyRegistry)
    }
}