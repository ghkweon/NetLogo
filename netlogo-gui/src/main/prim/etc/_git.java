// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.prim.etc;

import org.nlogo.api.LogoException;
import org.nlogo.core.Syntax;
import org.nlogo.nvm.Command;
import org.nlogo.nvm.Context;
import org.nlogo.nvm.RuntimePrimitiveException;

public final class _git
    extends Command {


  @Override
  public void perform(final Context context)
      throws LogoException {
    String command = argEvalString(context, 0);
    if (!System.getProperty("os.name").startsWith("Mac")) {
      throw new RuntimePrimitiveException
          (context, this, "at present, only works on Macs");
    }
    String dir = workspace.getModelDir();
    if (dir == null) {
      throw new RuntimePrimitiveException
          (context, this, "must save model first");
    }
    try {
      java.io.File git = new java.io.File(".git");
      if (!git.exists() || !git.isDirectory()) {
        throw new RuntimePrimitiveException
            (context, this, "no .git directory found");
      }
      Runtime.getRuntime().exec
          (new String[]
              {"osascript",
                  "-e", "tell application \"Terminal\"",
                  "-e", "activate",
                  // the /dev/null here is to suppress the output of chpwd
                  "-e", "do script with command \"cd \\\"" + dir + "\\\" > /dev/null ; " +
                  "git " + command + " \\\"" + workspace.getModelFileName() + "\\\" ; exit\"",
                  "-e", "end tell"});
    } catch (java.io.IOException ex) {
      throw new RuntimePrimitiveException(context, this, ex.getMessage());
    }
    context.ip = next;
  }
}
