package fr.utopics.eikins.mutation.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import fr.utopics.eikins.mutation.item.ItemMutagen;

public class CommandAmplify extends CommandBase {

	private List aliases;

	public CommandAmplify()
	{
		this.aliases = new ArrayList();
		this.aliases.add("amplify");
	}
	
	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

	@Override
	public String getName() {
		return "amplify";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/amplify <amplifier>";
	}

	@Override
	public List getAliases() {
		return aliases;
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		if(args.length >= 1)
		{
			if(((EntityPlayer) sender).getHeldItem().getItem() != null)
			{
				if(((EntityPlayer) sender).getHeldItem().getItem() instanceof ItemMutagen)
				{
					((ItemMutagen)((EntityPlayer)sender).getHeldItem().getItem()).setAmplifier(((EntityPlayer)sender).getHeldItem(), Integer.valueOf(args[0]));
				}
			}
		}
	}
    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }



}
