<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
  "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
  </head>
  <body>
    <h1>Hello World!</h1>

    <table title="Sample Structures" border="1">
      <tr>
        <td>
          <img  width="200" height="200" src="./StructureServlet?smiles=COc1cccc2c1C(=O)c1c(O)c3c(c(O)c1C2=O)C[C@@](O)(C(=O)CO)C[C@@H]3OC1CC(N)C(O)C(C)O1&title=NSC705701"/>
        </td>
        <td>
          <img  width="200" height="200" src="./StructureServlet?smiles=O=C1Cc2c3cc([N+](=O)[O-])ccc3[nH]c2-c2ccccc2N1&title=NSC401005"/>
        </td>
        <td>
          <img  width="200" height="200" src="./StructureServlet?smiles=CC1COC2C3=C(C(=O)C=CC3=O)C3OC(=O)C4CC5CCC1C5C2C43&title=NSC743380"/>
        </td>
        <td>
          <img  width="200" height="200" src="./StructureServlet?smiles=OCc1cn(Cc2cccc(Cl)c2)c2ccccc12&title=NSC163027"/>
        </td>
      </tr>
    </table>

  </body>
</html>
