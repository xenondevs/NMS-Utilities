package xyz.xenondevs.nmsutils.internal.util

import net.minecraft.advancements.Advancement
import net.minecraft.advancements.critereon.EntityPredicate
import net.minecraft.advancements.critereon.LighthingBoltPredicate
import net.minecraft.advancements.critereon.MinMaxBounds
import net.minecraft.server.network.ServerConnectionListener
import org.bukkit.craftbukkit.v1_19_R1.tag.CraftTag
import xyz.xenondevs.nmsutils.internal.util.ReflectionUtils.getClass
import xyz.xenondevs.nmsutils.internal.util.ReflectionUtils.getConstructor
import xyz.xenondevs.nmsutils.internal.util.ReflectionUtils.getField

@Suppress("MemberVisibilityCanBePrivate")
internal object ReflectionRegistry {
    
    // Classes
    val STATE_PROPERTIES_PREDICATE_CLASS = getClass("SRC(net.minecraft.advancements.critereon.StatePropertiesPredicate)")
    val EXACT_PROPERTY_MATCHER_CLASS = getClass("SRC(net.minecraft.advancements.critereon.StatePropertiesPredicate\$ExactPropertyMatcher)")
    val RANGED_PROPERTY_MATCHER_CLASS = getClass("SRC(net.minecraft.advancements.critereon.StatePropertiesPredicate\$RangedPropertyMatcher)")
    
    // Constructors
    val ADVANCEMENT_BUILDER_CONSTRUCTOR = getConstructor(Advancement.Builder::class.java, true)
    val STATE_PROPERTIES_PREDICATE_CONSTRUCTOR = getConstructor(STATE_PROPERTIES_PREDICATE_CLASS, true, List::class.java)
    val EXACT_PROPERTY_MATCHER_CONSTRUCTOR = getConstructor(EXACT_PROPERTY_MATCHER_CLASS, true, String::class.java, String::class.java)
    val RANGED_PROPERTY_MATCHER_CONSTRUCTOR = getConstructor(RANGED_PROPERTY_MATCHER_CLASS, true, String::class.java, String::class.java, String::class.java)
    val LIGHTNING_BOLT_PREDICATE_CONSTRUCTOR = getConstructor(LighthingBoltPredicate::class.java, true, MinMaxBounds.Ints::class.java, EntityPredicate::class.java)
    
    // Fields
    val CRAFT_TAG_TAG_KEY_FIELD = getField(CraftTag::class.java, true, "tag")
    val SERVER_CONNECTION_LISTENER_CHANNELS_FIELD = getField(ServerConnectionListener::class.java, true, "SRF(net.minecraft.server.network.ServerConnectionListener channels)")
    
}