package dev.schlaubi.alphabet

import dev.kord.common.Locale
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.channel

private const val commandName = "set-channel"

suspend fun Kord.registerCommands() {
    createGlobalApplicationCommands {
        input(commandName, "Sets the channel for monitoring") {
            dmPermission = false
            defaultMemberPermissions = Permissions(Permission.ManageGuild)
            descriptionLocalizations = mutableMapOf(Locale.GERMAN to "Setzt den Kanal der überwacht werden soll")

            channel("channel", "The channel to be monitored") {
                descriptionLocalizations = mutableMapOf(Locale.GERMAN to "Der Kanal, der überwacht werden soll")
                channelTypes = listOf(ChannelType.GuildText)
            }
        }
    }

    on<GuildChatInputCommandInteractionCreateEvent> {
        if (interaction.command.rootName == commandName) {
            val channel = interaction.resolvedObjects!!.channels!![interaction.command.options["channel"]!!.value]!!

            interaction.guild.saveSettings(interaction.guild.retrieveSettings().copy(channel = channel.id))

            interaction.respondEphemeral {
                content = "Successfully set channel to: ${channel.mention}"
            }
        }
    }
}
