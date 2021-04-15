package io.gamefreak.pixelmonextension.commands;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import io.gamefreak.pixelmonextension.Pixelmonextension;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CommandRegistry {


        public static void register() {

            CommandSpec maincommand = CommandSpec.builder()
                    .child(CommandSpec.builder()
                            .executor(new GiveToken())
                            .arguments(
                                    GenericArguments.enumValue(Text.of("token"), TokenTypes.TokenName.class),
                                    GenericArguments.optionalWeak(GenericArguments.user(Text.of("player"))) ,
                                    GenericArguments.optionalWeak(GenericArguments.integer(Text.of("amount")))

                            )
                            .description(Text.of(TextColors.GOLD, "Give token"))
                            .permission("pixelmonextension.admin.give").build(), "give")
                    .child(CommandSpec.builder()
                            .executor(new Balance())
                            .arguments(
                                    GenericArguments.optional(GenericArguments.user(Text.of("player")))
                            )
                            .description(Text.of(TextColors.GOLD, "Read tokens and the amount"))
                            .permission("pixelmonextension.balance.base").build(), "bal","balance","list")
                    .child(CommandSpec.builder()
                            .executor(new ClaimToken())
                            .arguments(
                                    GenericArguments.firstParsing(GenericArguments.enumValue(Text.of("token"), TokenTypes.TokenName.class)),
                                    GenericArguments.optional(GenericArguments.integer(Text.of("amount")))
                            )
                            .description(Text.of(TextColors.GOLD, "Read tokens and the amount"))
                            .permission("pixelmonextension.claim").build(), "claim")
                    .child(CommandSpec.builder()
                            .executor(new ChangeCatchrate())
                            .arguments(
                                    GenericArguments.firstParsing(GenericArguments.enumValue(Text.of("species"), EnumSpecies.class)),
                                    GenericArguments.optional(GenericArguments.integer(Text.of("rate")))
                            )
                            .description(Text.of(TextColors.GOLD, "Change catchrate of a pokemon"))
                            .permission("pixelmonextension.admin.changecatchrate").build(), "changecatchrate")
                    .child(CommandSpec.builder()
                            .executor(new ReadCatchrate())
                            .description(Text.of(TextColors.GOLD, "Get updated catchrates"))
                            .permission("pixelmonextension.admin.readcatchrate").build(), "readcatchrates")
                    .child(CommandSpec.builder()
                            .executor(new Reload())
                            .description(Text.of(TextColors.GOLD, "Reloads configs"))
                            .permission("pixelmonextension.admin.reload").build(), "reload")
                    .child(
                            CommandSpec.builder()
                                    .executor(new Convert())
                                    .description(Text.of(TextColors.GOLD,"Convert between physical and virtual tokens"))
                                    .arguments(
                                            GenericArguments.optionalWeak(GenericArguments.enumValue(Text.of("token"), TokenTypes.TokenName.class)),
                                            GenericArguments.optionalWeak(GenericArguments.integer(Text.of("amount")))
                                    ).permission("pixelmonextension.convert")
                                    .build(),
                            "convert"
                    )

                    .build();



            Sponge.getCommandManager().register(Pixelmonextension.INSTANCE, maincommand , "token","tk");
    }
}
