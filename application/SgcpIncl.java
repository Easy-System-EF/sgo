package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import sgo.model.entities.Grupo;
import sgo.model.entities.Material;
import sgo.model.services.GrupoService;
import sgo.model.services.MaterialService;

public class SgcpIncl implements Serializable {

 	 	private static final long serialVersionUID = 1L;
 
	 		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
 	 		
 	 		String pathF = "c:\\ARQS\\TEMP\\FORTXT.TXT";
			String pathC = "c:\\ARQS\\TEMP\\COMTXT.TXT";
			String pathCli = "c:\\ARQS\\TEMP\\CLITXT.TXT";
			String pathMat = "c:\\ARQS\\TEMP\\MATTXT.TXT";
//			List<Fornecedor> forn = new ArrayList<Fornecedor>();
//			List<Compromisso> com = new ArrayList<Compromisso>();
//			List<TipoFornecedor> tipo = new ArrayList<TipoFornecedor>();
//			List<ParPeriodo> par = new ArrayList<ParPeriodo>();
 //			List<Parcela> parc   = new ArrayList<Parcela>();
			List<Material> cli = new ArrayList<Material>();
 			List<Grupo> gru = new ArrayList<>();
 			
			int i = 221;
			int cont = 0;
			
//	 		Fornecedor objF = new Fornecedor();
//	 		Compromisso objC = new Compromisso();
//	 		TipoFornecedor objT = new TipoFornecedor();
//	 		ParPeriodo objP = new ParPeriodo();
//	 		Parcela objParc = new Parcela();
//	 		Cliente	objCli = new Cliente(); 
			Material objMat = new Material(); 
			Grupo objGru = new Grupo(); 
	 		

//			private FornecedorService forService;
//			private CompromissoService comService;
//			private TipoFornecedorService tipoService;
//			private ParPeriodoService parService;
//			private ParcelaService parcService;
//			private ClienteService cliService;
			private MaterialService matService;
			private GrupoService gruService;

	 	 	public void setGrupoService(GrupoService gruService) {
	 	 		this.gruService = gruService;
	 	 	}

	 	 	public void setMaterialService(MaterialService matService) {
	 	 		this.matService = matService;
	 	 	}
 	 	 	
//	 	 	public void setFornecedorService(FornecedorService forService) {
//	 	 		this.forService = forService;
//	 	 	}
	 	 	
//	 	 	public void setCompromissoService(CompromissoService comService) {
//	 	 		this.comService = comService;
//	 	 	}
//	 	 	
//	 	 	public void setTipoFornecedorService(TipoFornecedorService tipoService) {
//	 	 		this.tipoService = tipoService;
//	 	 	}
//	 	 	
//	 	 	public void setParPeriodoService(ParPeriodoService parService) {
//	 	 		this.parService = parService;
//	 	 	}
//	 	 	
//	 	 	public void setParcelaService(ParcelaService parcService) {
//	 	 		this.parcService = parcService;
//	 	 	}
//	 	 	
//	 	 	public void setClienteService(ClienteService cliService) {
//	 	 		this.cliService = cliService;
//	 	 	}
	 	 	
	 	 	public void incluir() {
 				setMaterialService(new MaterialService());
// 				setFornecedorService(new FornecedorService());
//	 				setCompromissoService(new CompromissoService());
//	 				setTipoFornecedorService(new TipoFornecedorService());
// 	 				setParPeriodoService(new ParPeriodoService());
//	 				setParcelaService(new ParcelaService());
//	 	 			setClienteService(new ClienteService());
//	 	 		inclusaoFornecedor();
//	 			deletaCompromisso();	
//	 	 		inclusaoCompromisso();
// 	 	 		inclusaoParcela();
//	 	 		inclusaoCliente();	
 				inclusaoMaterial();
	 	 	}
	 	 	
 	 	 	private void inclusaoMaterial() {
	 	 		try 
	 	 		{	BufferedReader brMat = new BufferedReader(new FileReader(pathMat));
	 				String lineMat = brMat.readLine();
	 				gruService = new GrupoService();
	 				objGru = new Grupo();
 	 	 			while (lineMat != null) 
	 	 			{	String[] campos = lineMat.split(";");
  	 	 				Integer codMat = null;
  	 	 				Integer gruMat = Integer.parseInt(campos[0]);
	 	 				String nomeMat = campos[1];
	 	 				double entMat = Double.parseDouble(campos[2]);
	 	 				double saiMat = 0.0;
	 	 				double sldMat = Double.parseDouble(campos[2]);
	 	 				double preMat = Double.parseDouble(campos[3]);
	 	 				double vdaMat = Double.parseDouble(campos[4]);
	 	 				Integer kmMat = Integer.parseInt(campos[5]);
	 	 				Integer mesMat = Integer.parseInt(campos[6]);
	 	 				double cmmMat = Double.parseDouble(campos[7]);
       	  	 			objGru = gruService.findById(gruMat);
       	  	 			objMat.setCodigoMat(codMat);
	 	 				objMat.setGrupoMat(gruMat);
	 	 				objMat.setNomeMat(nomeMat);
	 	 				objMat.setEntradaMat(entMat);
	 	 				objMat.setSaidaMat(saiMat);
	 	 				objMat.setPrecoMat(preMat);
	 	 				objMat.setVendaMat(vdaMat);
	 	 				objMat.setVidaKmMat(kmMat);
	 	 				objMat.setVidaMesMat(mesMat);
	 	 				objMat.setCmmMat(cmmMat);
	 	 				objMat.setGrupo(objGru);
	 	 				
 	 	 				matService.saveOrUpdate(objMat);
 	  	 				lineMat = brMat.readLine();
	 	 			}
	 	 			brMat.close();
//  	 	 		} catch (Illegal ArgumentException e1) {
//	 	 			System.out.println("Argumento " + e1.getMessage());
	 	 		} catch (java.util.InputMismatchException e2) {
	 	 			System.out.println("Erro input - " + e2.getMessage());
	 	 		} catch (IOException e) { 
	 	 			System.out.println("io " + e.getMessage());
	 	 		} finally {
	 	 			System.out.println("Kbou!!!");
	  	 		}
	 	 	}
 
	 	 	
/*
	 	 	private void inclusaoCliente() {
	 	 		try 
	 	 		{	BufferedReader brCli = new BufferedReader(new FileReader(pathCli));
	 				String lineCli = brCli.readLine();
	 	 			while (lineCli != null) 
	 	 			{	String[] campos = lineCli.split(";");
	 	 				String[] nome = lineCli.split(" ");
	 	 				System.out.println(lineCli);
 	 	 				Integer codCl = null;
	 	 				String nomeCl = campos[0];
	 	 				String endCl = campos[1];
	 	 				Integer numCl = 0;
	 	 				String comCl = null;
 	 	 				String baiCl = campos[3];
 	 	 				String cidCl = campos[4];
 	 	 				String cepCl = campos[5];
   	 	 				String ufCl = campos[6];
	 	 				int ddd1Cl = 31;
	 	 				int tel1Cl = Integer.parseInt(campos[7]);
	 	 				int ddd2Cl = 31;
	 	 				int tel2Cl = Integer.parseInt(campos[8]);
   	  	 				String emailCl = nome[0].toLowerCase() + "@gmail.com";
   	  	 				String pesCl = null;
	 	 				String sx = campos[11];
	 	 				if (sx == "J")
	 	 				{	pesCl = sx;
	 	 				}	else
	 	 					{	pesCl = "F";
	 	 					}
  	  	 				String cpfCl = "11111111111";
  	  	 				String cnpjCl = "22222222222222";
//   	  	 				if (pesCl == "F")
//  	  	 				{	cpfCl = campos[13];
//   	  	 				}
//   	  	 				if (pesCl == "J")
//  	  	 				{	cnpjCl = campos[13];
//   	  	 				}
    	  	 				
 	  	 				objCli.setCodigoCli(codCl);
	 	 				objCli.setNomeCli(nomeCl); 
	 	 				objCli.setRuaCli(endCl); 
	 	 				objCli.setNumeroCli(numCl);
	 	 				objCli.setComplementoCli(comCl);
	 	 				objCli.setBairroCli(baiCl);
	 	 				objCli.setCidadeCli(cidCl);
	 	 				objCli.setUfCli(ufCl);
	 	 				objCli.setCepCli(cepCl);
	 	 				objCli.setDdd01Cli(ddd1Cl);
	 	 				objCli.setTelefone01Cli(tel1Cl);
	 	 				objCli.setDdd02Cli(ddd2Cl);
	 	 				objCli.setTelefone02Cli(tel2Cl);
 	  	 				objCli.setEmailCli(emailCl);
 	 	 				objCli.setPessoaCli(pesCl.charAt(0));
	 	 				objCli.setCpfCli(cpfCl);
	 	 				objCli.setCnpjCli(cnpjCl);
  
 	 	 				cliService.saveOrUpdate(objCli);
	  	 				lineCli = brCli.readLine();
	 	 			}
	 	 			brCli.close();
  	 	 		} catch (IllegalArgumentException e1) {
	 	 			System.out.println("Argumento " + e1.getMessage());
	 	 		} catch (java.util.InputMismatchException e2) {
	 	 			System.out.println("Erro input - " + e2.getMessage());
	 	 		} catch (IOException e) {
	 	 			System.out.println("io " + e.getMessage());
	 	 		} finally {
	 	 			System.out.println("Kbou!!!");
	  	 		}
	 	 	}
 
			public void inclusaoFornecedor() {
	 	 		try 
	 	 		{	BufferedReader brF = new BufferedReader(new FileReader(pathF));
	 	 			String lineF = brF.readLine();
	 	 			while (lineF != null) 
	 	 			{	String[] campos = lineF.split(";");
	 	 				String[] nome = lineF.split(" ");
 	 	 				Integer codF = null;
	 	 				String nomeF = campos[1];
	 	 				String endF = campos[2];
	 	 				Integer numF = Integer.parseInt(campos[3]);
	 	 				String comF = null;
 	 	 				String baiF = campos[5];
 	 	 				String cidF = campos[6];
   	 	 				String ufF = campos[7];
	 	 				int cepF = Integer.parseInt(campos[8]);
	 	 				int tel1F = Integer.parseInt(campos[9]);
	 	 				int dd1F = Integer.parseInt(campos[10]);
	 	 				String contF = campos[11];
	 	 				Integer telcF = Integer.parseInt(campos[12]);
 	 	 				Integer ddcF = Integer.parseInt(campos[13]);
 	  	 				String emailF = nome[1].toLowerCase() + "@gmail.com";
 	  	 				String pixF = nome[1].toLowerCase() + "@gmail.com";
 	 	 				
 	  	 				objF.setCodigo(codF);
	 	 				objF.setRazaoSocial(nomeF); 
	 	 				objF.setRua(endF);
	 	 				objF.setNumero(numF);
	 	 				objF.setComplemento(comF);
	 	 				objF.setBairro(baiF);
	 	 				objF.setCidade(cidF);
	 	 				objF.setUf(ufF);
	 	 				objF.setCep(cepF);
	 	 				objF.setDdd01(dd1F);
	 	 				objF.setTelefone01(tel1F);
	 	 				objF.setDdd02(0);
	 	 				objF.setTelefone02(0);
	 	 				objF.setContato(contF);
	 	 				objF.setDddContato(ddcF);
	 	 				objF.setTelefoneContato(telcF);
	  	 				objF.setEmail(emailF);
	 	 				objF.setPix(pixF);
	 	 				objF.setObservacao(null);
	 	 				
// 	 	 				forService.saveOrUpdate(objF);
	  	 				lineF = brF.readLine();
	 	 			}
	 	 			brF.close();
 	 	 		} catch (IllegalArgumentException e1) {
	 	 			System.out.println("Argumento " + e1.getMessage());
	 	 		} catch (java.util.InputMismatchException e2) {
	 	 			System.out.println("Erro input - " + e2.getMessage());
	 	 		} catch (IOException e) {
	 	 			System.out.println("io " + e.getMessage());
	 	 		} finally {
	 	 			System.out.println("Kbou!!!");
	  	 		}
	 	 	}
*/
/*	 	 	
	 	 	private void deletaCompromisso() {
	 	 		parc = parcService.findAllPago();
  	 	 		for (Parcela p : parc)
	 	 		{	objParc.setCodigoFornecedorPar(p.getCodigoFornecedorPar());
	 	 			objParc.setNnfPar(p.getNnfPar());
 	 	 			System.out.println(objParc.getNnfPar());
//	 	 			parcService.removeNnf(objParc);
	 	 		}	
	 	 		parc = parcService.findAllAberto();
  	 	 		for (Parcela p : parc)
	 	 		{	objParc.setCodigoFornecedorPar(p.getCodigoFornecedorPar());
	 	 			objParc.setNnfPar(p.getNnfPar());
 	 	 			System.out.println(objParc.getNnfPar());
//	 	 			parcService.removeNnf(objParc);
	 	 		}	
		 	 	com  = comService.findAll();
		 	 	for (Compromisso co : com)
		 	 	{	objC.setNnfCom(co.getNnfCom());
	 	 			objC.setCodigoFornecedorCom(co.getCodigoFornecedorCom());
  	 	 			System.out.println(objC.getNnfCom());
//	 	 			comService.remove(objC);
 	 	 		}
	 	 	}
	 	 	
	 	 	private void inclusaoCompromisso() {
	 	 		try 
	 	 		{	BufferedReader brC = new BufferedReader(new FileReader(pathC));
	 	 			String lineC = brC.readLine();
	 	 			int ii = 0;
	  	 			while (ii < 43) 
	 	 			{	String[] campos = lineC.split(";");
 	 	 				i = i + 1;
	 	 				Integer cpoCod = i;
 	 	 				if (i > 231)
	  	 				{	i = 222;
	  	 					cpoCod = i;
 	  	 				}
 	 	 				ii += 1;
 	  	 				String cpoNome = "a";
	  	 				System.out.println(ii + " " + cpoCod + " " + lineC);
  	  	 	 			objF = forService.findById(cpoCod);
	  	 	 			if (objF.getRazaoSocial() != null)
	   	 	 			{	cpoNome = objF.getRazaoSocial(); }
 	 	 				int cpoNnf = Integer.parseInt(campos[1]);
 	 	 				Date cpoDtc = sdf.parse(campos[2]);
	 					Date cpoDtv = sdf.parse(campos[3]);
  	 	 				double cpoVlr = Double.parseDouble(campos[4]);
 	 	 				int cpoPar = Integer.parseInt(campos[5]);
	 	 				int cpoPra = Integer.parseInt(campos[6]);
 	 	 				int cpoTip = Integer.parseInt(campos[7]);
	 	 				objT = tipoService.findById(cpoTip);
	 	 				objP = parService.findById(1);

 	 	 				com.add(new Compromisso(cpoCod, cpoNome, cpoNnf, cpoDtc, cpoDtv, cpoVlr, 
	 	 						cpoPar, cpoPra, objF, objT, objP));
 
	 	 				objC.setCodigoFornecedorCom(cpoCod);
	 	 				objC.setNomeFornecedorCom(cpoNome);
	 	 				objC.setNnfCom(cpoNnf);
	  	 				objC.setDataCom(cpoDtc);
	 	 				objC.setDataVencimentoCom(cpoDtv);
	 	 				objC.setValorCom(cpoVlr);
	 	 				objC.setParcelaCom(cpoPar);
	 	 				objC.setPrazoCom(cpoPra);
	 	 				objC.setTipoFornecedor(objT);
	 	 				objC.setFornecedor(objF);
	 	 				objC.setParPeriodo(objP);
	 				
// 	 	 				comService.saveOrUpdate(objC);
 	  	 				lineC = brC.readLine();
	 	 			}
// 	 				for (Compromisso c : com)
// 	 				{	System.out.println(c);
// 	 				}
 	 				
	 	 			brC.close();
	 	 		} catch (IllegalArgumentException e1) {
	 	 			System.out.println("Argumento " + e1.getMessage());
	 	 		} catch (java.util.InputMismatchException e2) {
	 	 			System.out.println("Erro input - " + e2.getMessage());
	 	 		} catch (IOException e) {
	 	 			System.out.println("io " + e.getMessage());
	 	 		} catch (ParseException e) {
	 				System.out.println("Erro!!! Data inválida - " + e.getMessage());
	 				e.printStackTrace();
	  	 		} finally {
	 	 			System.out.println("Kbou!!!");
	  	 		}
	 	 	}
 			

	private void inclusaoParcela() {
	 		try 
	 		{	 	Calendar cal = Calendar.getInstance();
 	 				com  = comService.findAll();
  	 				for (Compromisso c : com)
	 				{  	cal.setTime(c.getDataVencimentoCom());
	 					int par = c.getParcelaCom() + 1;
	 					cont += 1;
	 					for (int i = 1; i < par; i ++)
	 					{	if (i > 1)
	 						{	cal.add(Calendar.DAY_OF_MONTH, (c.getPrazoCom()));
	 						}
	 					    objParc.setCodigoFornecedorPar(c.getCodigoFornecedorCom());
	 					    objParc.setNomeFornecedorPar(c.getNomeFornecedorCom());
	 					    objParc.setNnfPar(c.getNnfCom());
	 					    objParc.setNumeroPar(i); 
	 					    objParc.setDataVencimentoPar(cal.getTime());
  	 						if (c.getParcelaCom() > 1)
	 					    { 	objParc.setValorPar(objParc.calculaParcelas(c.getValorCom(), c.getParcelaCom(), i));
 						    }	else
 						    	{	objParc.setValorPar(c.getValorCom());
 						    	}
  							objParc.setTotalPar(objParc.getValorPar()); 
							objParc.setDescontoPar(0.00);
							objParc.setJurosPar(0.00);
							objParc.setPagoPar(0.00);
						    objParc.setDataPagamentoPar(cal.getTime());
						    objParc.setFornecedor(c.getFornecedor());
						    objParc.setTipoFornecedor(c.getTipoFornecedor());
						    objParc.setPeriodo(c.getParPeriodo());
						    cal.setTime(objParc.getDataVencimentoPar());
//						    parcService.insert(objParc);
						    System.out.println(objParc);
 	 					}
	 				}
	 			
	 			} catch (IllegalArgumentException e1) {
	 				System.out.println("Argumento " + e1.getMessage());
	 			} catch (java.util.InputMismatchException e2) {
	 				System.out.println("Erro input - " + e2.getMessage());
	 			} finally {
	 				System.out.println("Kbou!!! " + cont);
	 			}
  		}
*/  		
	}	
