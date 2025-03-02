<?xml version="1.0" encoding="UTF-8"?>
<!-- generated with COPASI 4.44 (Build 295) (http://www.copasi.org) at 2024-11-26T19:16:46Z -->
<?oxygen RNGSchema="http://www.copasi.org/static/schema/CopasiML.rng" type="xml"?>
<COPASI xmlns="http://www.copasi.org/static/schema" versionMajor="4" versionMinor="44" versionDevel="295" copasiSourcesModified="0">
  <ListOfFunctions>
    <Function key="Function_13" name="Mass action (irreversible)" type="MassAction" reversible="false">
      <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Function_13">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2017-11-27T17:06:19Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:obo.sbo:SBO:0000041" />
  </rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Comment>
        <body xmlns="http://www.w3.org/1999/xhtml">
<b>Mass action rate law for first order irreversible reactions</b>
<p>
Reaction scheme where the products are created from the reactants and the change of a product quantity is proportional to the product of reactant activities. The reaction scheme does not include any reverse process that creates the reactants from the products. The change of a product quantity is proportional to the quantity of one reactant.
</p>
</body>
      </Comment>
      <Expression>
        k1*PRODUCT&lt;substrate_i>
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_80" name="k1" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_81" name="substrate" order="1" role="substrate"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_41" name="pH-1s-Logistic [1]" type="UserDefined" reversible="unspecified">
      <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Function_41">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2018-01-24T16:54:18Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        substrate1 * k /(1+EXP( - enhancer* (pH-pHlimit)))
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_275" name="substrate1" order="0" role="substrate"/>
        <ParameterDescription key="FunctionParameter_274" name="k" order="1" role="constant"/>
        <ParameterDescription key="FunctionParameter_273" name="enhancer" order="2" role="constant"/>
        <ParameterDescription key="FunctionParameter_272" name="pH" order="3" role="constant"/>
        <ParameterDescription key="FunctionParameter_266" name="pHlimit" order="4" role="constant"/>
      </ListOfParameterDescriptions>
    </Function>
  </ListOfFunctions>
  <Model key="Model_0" name="Plasma Membrane" simulationType="time" timeUnit="s" volumeUnit="ml" areaUnit="mm²" lengthUnit="mm" quantityUnit="mmol" type="stochastic" avogadroConstant="6.0221417899999999e+23">
    <MiriamAnnotation>
<rdf:RDF
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Model_0">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2017-01-18T16:23:27Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>

    </MiriamAnnotation>
    <Comment>
      Del paper de LODISH PNAS
Agredo dos reacciones
FreeFe con parámetros aproximados de Fig 2 t1/2 =1.5 min
FreeTf con parámetros donde se establece que apoTf (TfaEn) no pega a pH neutro al receptor  t1/2 = 16 sec
Ojo, como Tfa está en otro compartimiento (soluble) más grande, esto afecta tramendamente a la k cinética.  Para lograr ese t1/2 subí la k a 1E5

En reacciones que involucran más de un compartimiento, por ejemplo una proteína de membrana (como receptor Tf) que une o libera algo soluble como Tf o Fe, Observo que  desaparece el Volumen en la ecuación de velocidad y entonces hay que  hay que multilicar la constante de velocidad por el Volumen del compartimiento 
donde ocurre la reacción de disociaci''on.

No sé qué pasa cuando ocurre la asociación de uno de membrana y uno soluble.  Hay que multiplicar por un volumen, pero no sé por cual
PARAMETROS DE Tf CYCLING
FREE FE
Lodish Proc. Natl Acad. Sci. USA Vol. 80, pp. 2258-2262, April 1983 Cell Biology
Upon prolonged incubation at pH 5, all of the iron was released from membrane-bound transferrin with a first-order rate constant of about 0.46 or a half-time of 1.5 min (data not shown).
Otro paper posterior da velocidades diferentes para los dos hierros
entre 5.5  y  1.4 min-1  Puedo usar el menos rápido 1.4 min-1 =1.4/60 s-1
Tuve que volver a 1.4

FREE Tfa
lodish JBC
6 pg/ml (77 nM)ferrotransferrin, the half-time for binding is about 3.0 min,
Usa el producto de 77*E-9 * ln(2)/3(half-time).  Yo voy a usar ln(2)/3 =0.23 min-1 = 0.23/60 s-1
Tuve que volver a .23

El Fe debe salir al citosol, de otro modo vuelve a PM.  La cinética la saco de Published, JBC Papers in Press, June 26, 2012, DOI 10.1074/jbc.M112.364208
6.3E-3 s-1
Lo mismo, me quedo con 0.38 que es en minutos (idem 0.0063 *60)

Agrego disociación del coplejo TfRTf en PM y la reversión, pero las constantes de velocidad que funcionan son k1 disociación 0.001 * vol Membranas
k1 asociacióon 1E4 * vol Membranas

De paper The Journal of Experimental Medicine Volume 180 October 1994 1471-1483
obtengo kon y koff para SIINFKL (a pH 7.4 y T 25oC0
De paper Garstka et al. 10.1073/pnas.1416543112 obtengo que kon no se modifica mucho con T y que koff aumenta 5 veces (usan otro péptido y otra técnica, pero los valores no son tan distintos)
Con ello tomo a pH 7.4 y T 37 que es la que supone el paper
kon = 6E6 1/mM seg
koff = 1E-5*5 1/seg
Para ajustar que haya binding a 7.4 de acuerdo a estas ks pero no haya a pH5 uso dos logísticas

kmax/(1+EXP(-enhancer*(pH-pHlimit))

para kon uso kmax 6E6, enhancer 6.4 pHlimit 6.5
para koff uso kmax 0.02, enhancer -9.2 pHlimit 5.4

pH se controlacon RabD y RabA no con vATPase y NOX

El cerrado de MHC con péptido (forma pepMHC) o sin péptido (forma cMHC) tendrían la misma k.  Esto es como si los péptidos inespecíficos fueran 1 mM siempre y en todo compartimiento.  Para corregir esto supongo uM y hago kmax/1000 para la reacción
oMHCI -> cMHCI

Elimino vATPAse y NOX como metabolitos.  Con esto alivio las series y el programa corrió 180.000 ticks

elimino lyEnz y todas las hidrólisis.  La degradación corre por cuenta de de la clase de Lysosomal Digestion Step de Repast

import y export no dependen de pH como antes
    </Comment>
    <ListOfCompartments>
      <Compartment key="Compartment_0" name="soluble" simulationType="fixed" dimensionality="3" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Compartment_0">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2022-02-14T14:47:23Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </Compartment>
      <Compartment key="Compartment_1" name="membrane" simulationType="fixed" dimensionality="3" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Compartment_1">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2022-02-14T14:48:15Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </Compartment>
      <Compartment key="Compartment_2" name="imaginario" simulationType="fixed" dimensionality="3" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Compartment_2">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2022-02-14T13:30:36Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </Compartment>
    </ListOfCompartments>
    <ListOfMetabolites>
      <Metabolite key="Metabolite_0" name="protonEn" simulationType="reactions" compartment="Compartment_0" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_0">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2017-11-28T14:59:21Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_1" name="R-TfaEn" simulationType="reactions" compartment="Compartment_1" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_1">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2022-03-31T12:45:14Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_2" name="REn" simulationType="reactions" compartment="Compartment_1" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_2">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2022-03-31T12:45:04Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_3" name="TfaEn" simulationType="reactions" compartment="Compartment_0" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_3">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2022-03-31T12:45:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_4" name="R-TfEn" simulationType="reactions" compartment="Compartment_1" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_4">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2022-03-31T12:45:17Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_5" name="FeEn" simulationType="reactions" compartment="Compartment_0" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#Metabolite_5">
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_6" name="TfEn" simulationType="reactions" compartment="Compartment_0" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#Metabolite_6">
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
    </ListOfMetabolites>
    <ListOfModelValues>
      <ModelValue key="ModelValue_0" name="pH" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_0">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2017-11-27T17:03:45Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          -log(&lt;CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[protonEn],Reference=Concentration>*1e-3)/log(10)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_1" name="kpH" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_1">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2018-01-27T18:21:35Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_2" name="enhancer" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_2">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2018-01-26T11:02:39Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_3" name="acidpH" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_3">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2018-01-26T12:59:17Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_4" name="alkpH" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_4">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2022-02-16T10:43:15Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_5" name="enhancerNegative" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_5">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2018-01-26T11:02:39Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_6" name="kfreeFe" simulationType="assignment" addNoise="false">
        <Comment>
          El valor 1.66 sale del paper doi:10.1016/j.jmb.2009.11.023 tomando la t1/2 (25sec) de fig 3B para release of apoTf
        </Comment>
        <Expression>
          &lt;CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Reference=InitialVolume>*1.66/60
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_7" name="kfreeTfa" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_7">
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          El valor 2.8 del paper de Lodish (1/.36)
        </Comment>
        <Expression>
          &lt;CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Reference=InitialVolume>*2.8/60
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_8" name="kdisTf" simulationType="assignment" addNoise="false">
        <Comment>
          No borro lo que está abajo, pero observo que se usó 0.1 que  es el valor de Lodish Tabe II B


Este parámetro no se ajustó al 0.1 que está en Lodish JBC porque se disociaba mucho.  Se ajustó para que se disocie un 30% que es lo que muestra este paper en fig3 cuando cuantifica el Fe radioactivo en el medio
O sea unas 50 veces más lerdo que k-1 del paper
        </Comment>
        <Expression>
          &lt;CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Reference=InitialVolume>*0.1/60
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_9" name="kBindTf" simulationType="assignment" addNoise="false">
        <Comment>
          La k+1 se ajusta de auerdo a la Kd(equilibrio) de Tf+R - Tf.R 13E-9 M (lodish PNAS) o sea 13E-6 mM
k+1 es kin= koff/Kd

La k-1 se ajusta para que no se libere más del 30% al iniciar la reacción
Pero con estos valores de kin, la koff se pudo utilizar la de Lodish Table II, B
        </Comment>
        <Expression>
          &lt;CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Reference=InitialVolume>*&lt;CN=Root,Model=Plasma Membrane,Vector=Values[kdisTf],Reference=InitialValue>/13E-9
        </Expression>
      </ModelValue>
    </ListOfModelValues>
    <ListOfReactions>
      <Reaction key="Reaction_0" name="FreeTfa" reversible="false" fast="false" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_0">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2022-03-31T13:27:08Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_1" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_2" stoichiometry="1"/>
          <Product metabolite="Metabolite_3" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_7152" name="k" value="2.80933e+06"/>
          <Constant key="Parameter_7155" name="pHlimit" value="7.3"/>
          <Constant key="Parameter_7153" name="enhancer" value="18"/>
          <Constant key="Parameter_7156" name="pH" value="7.40012"/>
        </ListOfConstants>
        <KineticLaw function="Function_41" unitType="Default" scalingCompartment="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_275">
              <SourceParameter reference="Metabolite_1"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_274">
              <SourceParameter reference="ModelValue_7"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_273">
              <SourceParameter reference="Parameter_7153"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_272">
              <SourceParameter reference="ModelValue_0"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_266">
              <SourceParameter reference="Parameter_7155"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_1" name="FreeFe" reversible="false" fast="false" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_1">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2022-03-31T13:27:12Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_4" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_1" stoichiometry="1"/>
          <Product metabolite="Metabolite_5" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4611" name="enhancer" value="-18"/>
          <Constant key="Parameter_4613" name="pHlimit" value="6.7"/>
          <Constant key="Parameter_4610" name="k" value="1.66553e+06"/>
          <Constant key="Parameter_8419" name="pH" value="7.40012"/>
        </ListOfConstants>
        <KineticLaw function="Function_41" unitType="Default" scalingCompartment="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_275">
              <SourceParameter reference="Metabolite_4"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_274">
              <SourceParameter reference="ModelValue_6"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_273">
              <SourceParameter reference="Parameter_4611"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_272">
              <SourceParameter reference="ModelValue_0"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_266">
              <SourceParameter reference="Parameter_4613"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_2" name="disTf" reversible="false" fast="false" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_2">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2022-03-31T17:33:13Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_4" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_2" stoichiometry="1"/>
          <Product metabolite="Metabolite_6" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_8416" name="k1" value="100333"/>
        </ListOfConstants>
        <KineticLaw function="Function_13" unitType="Default" scalingCompartment="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_80">
              <SourceParameter reference="ModelValue_8"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_81">
              <SourceParameter reference="Metabolite_4"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_3" name="BindTf" reversible="false" fast="false" addNoise="false">
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_2" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_6" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_4" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4612" name="k1" value="4.64621e+20"/>
        </ListOfConstants>
        <KineticLaw function="Function_13" unitType="Default" scalingCompartment="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_80">
              <SourceParameter reference="ModelValue_9"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_81">
              <SourceParameter reference="Metabolite_2"/>
              <SourceParameter reference="Metabolite_6"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
    </ListOfReactions>
    <ListOfModelParameterSets activeSet="ModelParameterSet_0">
      <ModelParameterSet key="ModelParameterSet_0" name="Initial State">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelParameterSet_0">
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ModelParameterGroup cn="String=Initial Time" type="Group">
          <ModelParameter cn="CN=Root,Model=Plasma Membrane" value="0" type="Model" simulationType="time"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Compartment Sizes" type="Group">
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble]" value="24000000000" type="Compartment" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane]" value="60200000" type="Compartment" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[imaginario]" value="1" type="Compartment" simulationType="fixed"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Species Values" type="Group">
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[protonEn]" value="5.7523498378080003e+26" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[R-TfaEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[REn]" value="7.3522168481500742e+22" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[TfaEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[R-TfEn]" value="3.6724292521901228e+25" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[FeEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[TfEn]" value="6.0221466812355324e+30" type="Species" simulationType="reactions"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Global Quantities" type="Group">
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[pH]" value="7.4001169279263115" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[kpH]" value="0.00020000000000000001" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[enhancer]" value="6.4000000000000004" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[acidpH]" value="5.4000000000000004" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[alkpH]" value="6.5" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[enhancerNegative]" value="-9.1999999999999993" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[kfreeFe]" value="1665533.3333333333" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[kfreeTfa]" value="2809333.3333333335" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[kdisTf]" value="100333.33333333333" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[kBindTf]" value="4.6462051282051275e+20" type="ModelValue" simulationType="assignment"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Kinetic Parameters" type="Group">
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[FreeTfa]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[FreeTfa],ParameterGroup=Parameters,Parameter=k" value="2809333.3333333335" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Plasma Membrane,Vector=Values[kfreeTfa],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[FreeTfa],ParameterGroup=Parameters,Parameter=pHlimit" value="7.2999999999999998" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[FreeTfa],ParameterGroup=Parameters,Parameter=enhancer" value="18" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[FreeTfa],ParameterGroup=Parameters,Parameter=pH" value="7.4001169279263115" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Plasma Membrane,Vector=Values[pH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[FreeFe]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[FreeFe],ParameterGroup=Parameters,Parameter=enhancer" value="-18" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[FreeFe],ParameterGroup=Parameters,Parameter=pHlimit" value="6.7000000000000002" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[FreeFe],ParameterGroup=Parameters,Parameter=k" value="1665533.3333333333" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Plasma Membrane,Vector=Values[kfreeFe],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[FreeFe],ParameterGroup=Parameters,Parameter=pH" value="7.4001169279263115" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Plasma Membrane,Vector=Values[pH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[disTf]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[disTf],ParameterGroup=Parameters,Parameter=k1" value="100333.33333333333" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Plasma Membrane,Vector=Values[kdisTf],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[BindTf]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[BindTf],ParameterGroup=Parameters,Parameter=k1" value="4.6462051282051275e+20" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Plasma Membrane,Vector=Values[kBindTf],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
        </ModelParameterGroup>
      </ModelParameterSet>
      <ModelParameterSet key="ModelParameterSet_3" name="Parameter Set 2021-09-02 10:10:27">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelParameterSet_3">
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ModelParameterGroup cn="String=Initial Time" type="Group">
          <ModelParameter cn="CN=Root,Model=Plasma Membrane" value="0" type="Model" simulationType="time"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Compartment Sizes" type="Group">
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble]" value="1000000" type="Compartment" simulationType="fixed"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Species Values" type="Group">
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[ovaEn]" value="6.02214179e+17" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[exCxEn]" value="6022141790000000" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[ovaCy]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[bMGEn]" value="6022141790000000" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[pepEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[oMHCIEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[pepMHCIEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[protonEn]" value="602214179000000" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[protonCy]" value="60221417900000.023" type="Species" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[cMHCIEn]" value="6022141790000000" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[pepCy]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[imCxEn]" value="6022141790000000" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[imaginario],Vector=Metabolites[RabD]" value="1.0839855222e+18" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[imaginario],Vector=Metabolites[RabA]" value="60221417900000000" type="Species" simulationType="reactions"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Global Quantities" type="Group">
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[pH]" value="5.9999999999999991" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[kpH]" value="0.00020000000000000001" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[enhancer]" value="3" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[acidpH]" value="-1.5" type="ModelValue" simulationType="fixed"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Kinetic Parameters" type="Group">
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[OVAout]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[OVAout],ParameterGroup=Parameters,Parameter=k1" value="2.0000000000000002e-05" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-loading]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-loading],ParameterGroup=Parameters,Parameter=k" value="10" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-loading],ParameterGroup=Parameters,Parameter=pH" value="5.9999999999999991" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[pH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-loading],ParameterGroup=Parameters,Parameter=pHlimit" value="5.7999999999999998" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-loading],ParameterGroup=Parameters,Parameter=enhancer" value="3" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[enhancer],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonPumpRabD]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonPumpRabD],ParameterGroup=Parameters,Parameter=k1" value="0.00080000000000000004" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonLeak]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonLeak],ParameterGroup=Parameters,Parameter=k1" value="0.040000000000000001" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonLeak],ParameterGroup=Parameters,Parameter=k2" value="0.040000000000000001" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIopening]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIopening],ParameterGroup=Parameters,Parameter=k" value="9.9999999999999995e-07" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIopening],ParameterGroup=Parameters,Parameter=pH" value="5.9999999999999991" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[pH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIopening],ParameterGroup=Parameters,Parameter=pHlimit" value="6" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIopening],ParameterGroup=Parameters,Parameter=enhancer" value="-1.5" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[acidpH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-peptOpening]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-peptOpening],ParameterGroup=Parameters,Parameter=k" value="9.9999999999999995e-07" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-peptOpening],ParameterGroup=Parameters,Parameter=pH" value="5.9999999999999991" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[pH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-peptOpening],ParameterGroup=Parameters,Parameter=pHlimit" value="6" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-peptOpening],ParameterGroup=Parameters,Parameter=enhancer" value="-1.5" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[acidpH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonPumpRabA]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonPumpRabA],ParameterGroup=Parameters,Parameter=k1" value="5.0000000000000002e-05" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIclosing]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIclosing],ParameterGroup=Parameters,Parameter=k" value="0.01" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIclosing],ParameterGroup=Parameters,Parameter=pH" value="5.9999999999999991" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[pH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIclosing],ParameterGroup=Parameters,Parameter=pHlimit" value="5.7999999999999998" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIclosing],ParameterGroup=Parameters,Parameter=enhancer" value="3" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[enhancer],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[pepImport]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[pepImport],ParameterGroup=Parameters,Parameter=k1" value="0.00020000000000000001" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[proteosome]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[proteosome],ParameterGroup=Parameters,Parameter=k1" value="0.0001" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
        </ModelParameterGroup>
      </ModelParameterSet>
      <ModelParameterSet key="ModelParameterSet_4" name="Parameter Set 2021-09-03 10:07:48">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelParameterSet_4">
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ModelParameterGroup cn="String=Initial Time" type="Group">
          <ModelParameter cn="CN=Root,Model=Plasma Membrane" value="0" type="Model" simulationType="time"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Compartment Sizes" type="Group">
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble]" value="1000000" type="Compartment" simulationType="fixed"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Species Values" type="Group">
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[ovaEn]" value="6.02214179e+17" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[exCxEn]" value="6022141790000000" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[ovaCy]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[bMGEn]" value="6022141790000000" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[pepEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[oMHCIEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[pepMHCIEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[protonEn]" value="602214179000000" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[protonCy]" value="60221417900000.023" type="Species" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[cMHCIEn]" value="6022141790000000" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[pepCy]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[imCxEn]" value="6022141790000000" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[imaginario],Vector=Metabolites[RabD]" value="1.0839855222e+18" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[imaginario],Vector=Metabolites[RabA]" value="60221417900000000" type="Species" simulationType="reactions"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Global Quantities" type="Group">
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[pH]" value="5.9999999999999991" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[kpH]" value="0.00020000000000000001" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[enhancer]" value="3" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[acidpH]" value="-1.5" type="ModelValue" simulationType="fixed"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Kinetic Parameters" type="Group">
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[OVAout]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[OVAout],ParameterGroup=Parameters,Parameter=k1" value="0.002" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-loading]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-loading],ParameterGroup=Parameters,Parameter=k" value="10" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-loading],ParameterGroup=Parameters,Parameter=pH" value="5.9999999999999991" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[pH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-loading],ParameterGroup=Parameters,Parameter=pHlimit" value="5.7999999999999998" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-loading],ParameterGroup=Parameters,Parameter=enhancer" value="3" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[enhancer],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonPumpRabD]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonPumpRabD],ParameterGroup=Parameters,Parameter=k1" value="0.00080000000000000004" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonLeak]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonLeak],ParameterGroup=Parameters,Parameter=k1" value="0.040000000000000001" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonLeak],ParameterGroup=Parameters,Parameter=k2" value="0.040000000000000001" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIopening]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIopening],ParameterGroup=Parameters,Parameter=k" value="9.9999999999999995e-07" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIopening],ParameterGroup=Parameters,Parameter=pH" value="5.9999999999999991" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[pH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIopening],ParameterGroup=Parameters,Parameter=pHlimit" value="6" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIopening],ParameterGroup=Parameters,Parameter=enhancer" value="-1.5" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[acidpH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-peptOpening]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-peptOpening],ParameterGroup=Parameters,Parameter=k" value="9.9999999999999995e-07" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-peptOpening],ParameterGroup=Parameters,Parameter=pH" value="5.9999999999999991" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[pH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-peptOpening],ParameterGroup=Parameters,Parameter=pHlimit" value="6" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-peptOpening],ParameterGroup=Parameters,Parameter=enhancer" value="-1.5" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[acidpH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonPumpRabA]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonPumpRabA],ParameterGroup=Parameters,Parameter=k1" value="5.0000000000000002e-05" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIclosing]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIclosing],ParameterGroup=Parameters,Parameter=k" value="0.01" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIclosing],ParameterGroup=Parameters,Parameter=pH" value="5.9999999999999991" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[pH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIclosing],ParameterGroup=Parameters,Parameter=pHlimit" value="5.7999999999999998" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIclosing],ParameterGroup=Parameters,Parameter=enhancer" value="3" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[enhancer],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[pepImport]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[pepImport],ParameterGroup=Parameters,Parameter=k1" value="0.002" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[proteosome]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[proteosome],ParameterGroup=Parameters,Parameter=k1" value="0.0001" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
        </ModelParameterGroup>
      </ModelParameterSet>
      <ModelParameterSet key="ModelParameterSet_5" name="Parameter Set 2022-02-25 12:03:24">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelParameterSet_5">
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ModelParameterGroup cn="String=Initial Time" type="Group">
          <ModelParameter cn="CN=Root,Model=Plasma Membrane" value="0" type="Model" simulationType="time"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Compartment Sizes" type="Group">
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble]" value="4800000000" type="Compartment" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane]" value="2400000" type="Compartment" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[imaginario]" value="1" type="Compartment" simulationType="fixed"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Species Values" type="Group">
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[ovaEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[exCxEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[ovaCy]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[bMGEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[pepEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[oMHCIEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[pepMHCIEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[protonEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[protonCy]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[cMHCIEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[pepCy]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[imCxEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[imaginario],Vector=Metabolites[RabD]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[imaginario],Vector=Metabolites[RabA]" value="0" type="Species" simulationType="reactions"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Global Quantities" type="Group">
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[pH]" value="0" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[kpH]" value="0.00020000000000000001" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[enhancer]" value="6.4000000000000004" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[acidpH]" value="5.4000000000000004" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[RabDpump]" value="0" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[RabApump]" value="0" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[alkpH]" value="6.5" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[enhancerNegative]" value="-9.1999999999999993" type="ModelValue" simulationType="fixed"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Kinetic Parameters" type="Group">
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[OVAout]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[OVAout],ParameterGroup=Parameters,Parameter=k1" value="100000000000" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-loading]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-loading],ParameterGroup=Parameters,Parameter=k" value="6000000" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-loading],ParameterGroup=Parameters,Parameter=pH" value="0" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[pH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-loading],ParameterGroup=Parameters,Parameter=pHlimit" value="6.5" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[alkpH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-loading],ParameterGroup=Parameters,Parameter=enhancer" value="6.4000000000000004" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[enhancer],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonPumpRabD]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonPumpRabD],ParameterGroup=Parameters,Parameter=v" value="0" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[RabDpump],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonLeak]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonLeak],ParameterGroup=Parameters,Parameter=k1" value="0.040000000000000001" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonLeak],ParameterGroup=Parameters,Parameter=k2" value="0.040000000000000001" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIopening]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIopening],ParameterGroup=Parameters,Parameter=k" value="6000" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIopening],ParameterGroup=Parameters,Parameter=pH" value="0" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[pH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIopening],ParameterGroup=Parameters,Parameter=pHlimit" value="5.4000000000000004" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[acidpH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIopening],ParameterGroup=Parameters,Parameter=enhancer" value="-9.1999999999999993" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[enhancerNegative],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-peptOpening]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-peptOpening],ParameterGroup=Parameters,Parameter=k" value="6000" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-peptOpening],ParameterGroup=Parameters,Parameter=pH" value="0" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[pH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-peptOpening],ParameterGroup=Parameters,Parameter=pHlimit" value="5.4000000000000004" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[acidpH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-peptOpening],ParameterGroup=Parameters,Parameter=enhancer" value="-9.1999999999999993" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[enhancerNegative],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonPumpRabA]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonPumpRabA],ParameterGroup=Parameters,Parameter=v" value="0" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[RabApump],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIclosing]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIclosing],ParameterGroup=Parameters,Parameter=k" value="6000" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIclosing],ParameterGroup=Parameters,Parameter=pH" value="0" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[pH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIclosing],ParameterGroup=Parameters,Parameter=pHlimit" value="6.5" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[alkpH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIclosing],ParameterGroup=Parameters,Parameter=enhancer" value="6.4000000000000004" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[enhancer],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[pepImport]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[pepImport],ParameterGroup=Parameters,Parameter=k1" value="50000000000" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[proteosome]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[proteosome],ParameterGroup=Parameters,Parameter=k1" value="10" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
        </ModelParameterGroup>
      </ModelParameterSet>
      <ModelParameterSet key="ModelParameterSet_6" name="Parameter Set 2022-02-25 16:23:42">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelParameterSet_6">
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ModelParameterGroup cn="String=Initial Time" type="Group">
          <ModelParameter cn="CN=Root,Model=Plasma Membrane" value="0" type="Model" simulationType="time"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Compartment Sizes" type="Group">
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble]" value="4800000000" type="Compartment" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane]" value="2400000" type="Compartment" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[imaginario]" value="1" type="Compartment" simulationType="fixed"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Species Values" type="Group">
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[ovaEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[exCxEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[ovaCy]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[bMGEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[pepEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[oMHCIEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[pepMHCIEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[protonEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[protonCy]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[cMHCIEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[pepCy]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[imCxEn]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[imaginario],Vector=Metabolites[RabD]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[imaginario],Vector=Metabolites[RabA]" value="0" type="Species" simulationType="reactions"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Global Quantities" type="Group">
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[pH]" value="0" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[kpH]" value="0.00020000000000000001" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[enhancer]" value="6.4000000000000004" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[acidpH]" value="5.4000000000000004" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[RabDpump]" value="0" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[RabApump]" value="0" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[alkpH]" value="6.5" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Values[enhancerNegative]" value="-9.1999999999999993" type="ModelValue" simulationType="fixed"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Kinetic Parameters" type="Group">
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[OVAout]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[OVAout],ParameterGroup=Parameters,Parameter=k1" value="1000000" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-loading]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-loading],ParameterGroup=Parameters,Parameter=k" value="6000000" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-loading],ParameterGroup=Parameters,Parameter=pH" value="0" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[pH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-loading],ParameterGroup=Parameters,Parameter=pHlimit" value="6.5" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[alkpH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-loading],ParameterGroup=Parameters,Parameter=enhancer" value="6.4000000000000004" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[enhancer],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonPumpRabD]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonPumpRabD],ParameterGroup=Parameters,Parameter=v" value="0" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[RabDpump],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonLeak]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonLeak],ParameterGroup=Parameters,Parameter=k1" value="0.040000000000000001" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonLeak],ParameterGroup=Parameters,Parameter=k2" value="0.040000000000000001" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIopening]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIopening],ParameterGroup=Parameters,Parameter=k" value="6000" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIopening],ParameterGroup=Parameters,Parameter=pH" value="0" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[pH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIopening],ParameterGroup=Parameters,Parameter=pHlimit" value="5.4000000000000004" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[acidpH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIopening],ParameterGroup=Parameters,Parameter=enhancer" value="-9.1999999999999993" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[enhancerNegative],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-peptOpening]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-peptOpening],ParameterGroup=Parameters,Parameter=k" value="6000" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-peptOpening],ParameterGroup=Parameters,Parameter=pH" value="0" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[pH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-peptOpening],ParameterGroup=Parameters,Parameter=pHlimit" value="5.4000000000000004" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[acidpH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCI-peptOpening],ParameterGroup=Parameters,Parameter=enhancer" value="-9.1999999999999993" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[enhancerNegative],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonPumpRabA]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[ProtonPumpRabA],ParameterGroup=Parameters,Parameter=v" value="0" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[RabApump],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIclosing]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIclosing],ParameterGroup=Parameters,Parameter=k" value="6000" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIclosing],ParameterGroup=Parameters,Parameter=pH" value="0" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[pH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIclosing],ParameterGroup=Parameters,Parameter=pHlimit" value="6.5" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[alkpH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[MHCIclosing],ParameterGroup=Parameters,Parameter=enhancer" value="6.4000000000000004" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=AntigenPresentation,Vector=Values[enhancer],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[pepImport]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[pepImport],ParameterGroup=Parameters,Parameter=k1" value="100000" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[proteosome]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Plasma Membrane,Vector=Reactions[proteosome],ParameterGroup=Parameters,Parameter=k1" value="0.01" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
        </ModelParameterGroup>
      </ModelParameterSet>
    </ListOfModelParameterSets>
    <StateTemplate>
      <StateTemplateVariable objectReference="Model_0"/>
      <StateTemplateVariable objectReference="Metabolite_2"/>
      <StateTemplateVariable objectReference="Metabolite_1"/>
      <StateTemplateVariable objectReference="Metabolite_6"/>
      <StateTemplateVariable objectReference="Metabolite_5"/>
      <StateTemplateVariable objectReference="Metabolite_3"/>
      <StateTemplateVariable objectReference="Metabolite_4"/>
      <StateTemplateVariable objectReference="ModelValue_0"/>
      <StateTemplateVariable objectReference="ModelValue_6"/>
      <StateTemplateVariable objectReference="ModelValue_7"/>
      <StateTemplateVariable objectReference="ModelValue_8"/>
      <StateTemplateVariable objectReference="ModelValue_9"/>
      <StateTemplateVariable objectReference="Metabolite_0"/>
      <StateTemplateVariable objectReference="Compartment_0"/>
      <StateTemplateVariable objectReference="Compartment_1"/>
      <StateTemplateVariable objectReference="Compartment_2"/>
      <StateTemplateVariable objectReference="ModelValue_1"/>
      <StateTemplateVariable objectReference="ModelValue_2"/>
      <StateTemplateVariable objectReference="ModelValue_3"/>
      <StateTemplateVariable objectReference="ModelValue_4"/>
      <StateTemplateVariable objectReference="ModelValue_5"/>
    </StateTemplate>
    <InitialState type="initialState">
      0 7.3522168481500742e+22 0 6.0221466812355324e+30 0 0 3.6724292521901228e+25 7.4001169279263115 1665533.3333333333 2809333.3333333335 100333.33333333333 4.6462051282051275e+20 5.7523498378080003e+26 24000000000 60200000 1 0.00020000000000000001 6.4000000000000004 5.4000000000000004 6.5 -9.1999999999999993 
    </InitialState>
  </Model>
  <ListOfTasks>
    <Task key="Task_13" name="Steady-State" type="steadyState" scheduled="false" updateModel="false">
      <Report reference="Report_10" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="JacobianRequested" type="bool" value="1"/>
        <Parameter name="StabilityAnalysisRequested" type="bool" value="1"/>
      </Problem>
      <Method name="Enhanced Newton" type="EnhancedNewton">
        <Parameter name="Resolution" type="unsignedFloat" value="1.0000000000000001e-09"/>
        <Parameter name="Derivation Factor" type="unsignedFloat" value="0.001"/>
        <Parameter name="Use Newton" type="bool" value="1"/>
        <Parameter name="Use Integration" type="bool" value="1"/>
        <Parameter name="Use Back Integration" type="bool" value="1"/>
        <Parameter name="Accept Negative Concentrations" type="bool" value="0"/>
        <Parameter name="Iteration Limit" type="unsignedInteger" value="50"/>
        <Parameter name="Maximum duration for forward integration" type="unsignedFloat" value="1000000000"/>
        <Parameter name="Maximum duration for backward integration" type="unsignedFloat" value="1000000"/>
        <Parameter name="Target Criterion" type="string" value="Distance and Rate"/>
      </Method>
    </Task>
    <Task key="Task_12" name="Time-Course" type="timeCourse" scheduled="false" updateModel="false">
      <Report reference="Report_10" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="AutomaticStepSize" type="bool" value="0"/>
        <Parameter name="StepNumber" type="unsignedInteger" value="60"/>
        <Parameter name="StepSize" type="float" value="1"/>
        <Parameter name="Duration" type="float" value="60"/>
        <Parameter name="TimeSeriesRequested" type="bool" value="0"/>
        <Parameter name="OutputStartTime" type="float" value="0"/>
        <Parameter name="Output Event" type="bool" value="0"/>
        <Parameter name="Start in Steady State" type="bool" value="0"/>
        <Parameter name="Use Values" type="bool" value="0"/>
        <Parameter name="Values" type="string" value=""/>
        <Parameter name="Continue on Simultaneous Events" type="bool" value="0"/>
      </Problem>
      <Method name="Deterministic (LSODA)" type="Deterministic(LSODA)">
        <Parameter name="Integrate Reduced Model" type="bool" value="0"/>
        <Parameter name="Relative Tolerance" type="unsignedFloat" value="9.9999999999999995e-07"/>
        <Parameter name="Absolute Tolerance" type="unsignedFloat" value="9.9999999999999998e-13"/>
        <Parameter name="Max Internal Steps" type="unsignedInteger" value="100000"/>
        <Parameter name="Max Internal Step Size" type="unsignedFloat" value="0"/>
      </Method>
    </Task>
    <Task key="Task_11" name="Scan" type="scan" scheduled="false" updateModel="false">
      <Problem>
        <Parameter name="Subtask" type="unsignedInteger" value="1"/>
        <ParameterGroup name="ScanItems">
          <ParameterGroup name="ScanItem">
            <Parameter name="Maximum" type="float" value="0.0001"/>
            <Parameter name="Minimum" type="float" value="5.0000000000000002e-05"/>
            <Parameter name="Number of steps" type="unsignedInteger" value="20"/>
            <Parameter name="Object" type="cn" value="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[protonEn],Reference=InitialConcentration"/>
            <Parameter name="Type" type="unsignedInteger" value="1"/>
            <Parameter name="log" type="bool" value="1"/>
            <Parameter name="Use Values" type="bool" value="0"/>
            <Parameter name="Values" type="string" value=""/>
          </ParameterGroup>
        </ParameterGroup>
        <Parameter name="Subtask Output" type="string" value="subTaskDuring"/>
        <Parameter name="Adjust initial conditions" type="bool" value="0"/>
        <Parameter name="Continue on Error" type="bool" value="0"/>
      </Problem>
      <Method name="Scan Framework" type="ScanFramework">
      </Method>
    </Task>
    <Task key="Task_10" name="Elementary Flux Modes" type="fluxMode" scheduled="false" updateModel="false">
      <Report reference="Report_9" target="" append="1" confirmOverwrite="1"/>
      <Problem>
      </Problem>
      <Method name="EFM Algorithm" type="EFMAlgorithm">
      </Method>
    </Task>
    <Task key="Task_9" name="Optimization" type="optimization" scheduled="false" updateModel="false">
      <Report reference="Report_8" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="Subtask" type="cn" value="CN=Root,Vector=TaskList[Steady-State]"/>
        <ParameterText name="ObjectiveExpression" type="expression">
          
        </ParameterText>
        <Parameter name="Maximize" type="bool" value="0"/>
        <Parameter name="Randomize Start Values" type="bool" value="0"/>
        <Parameter name="Calculate Statistics" type="bool" value="1"/>
        <ParameterGroup name="OptimizationItemList">
        </ParameterGroup>
        <ParameterGroup name="OptimizationConstraintList">
        </ParameterGroup>
      </Problem>
      <Method name="Random Search" type="RandomSearch">
        <Parameter name="Log Verbosity" type="unsignedInteger" value="0"/>
        <Parameter name="Number of Iterations" type="unsignedInteger" value="100000"/>
        <Parameter name="Random Number Generator" type="unsignedInteger" value="1"/>
        <Parameter name="Seed" type="unsignedInteger" value="0"/>
      </Method>
    </Task>
    <Task key="Task_8" name="Parameter Estimation" type="parameterFitting" scheduled="false" updateModel="false">
      <Report reference="Report_7" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="Maximize" type="bool" value="0"/>
        <Parameter name="Randomize Start Values" type="bool" value="0"/>
        <Parameter name="Calculate Statistics" type="bool" value="1"/>
        <ParameterGroup name="OptimizationItemList">
        </ParameterGroup>
        <ParameterGroup name="OptimizationConstraintList">
        </ParameterGroup>
        <Parameter name="Steady-State" type="cn" value="CN=Root,Vector=TaskList[Steady-State]"/>
        <Parameter name="Time-Course" type="cn" value="CN=Root,Vector=TaskList[Time-Course]"/>
        <Parameter name="Create Parameter Sets" type="bool" value="0"/>
        <Parameter name="Use Time Sens" type="bool" value="0"/>
        <Parameter name="Time-Sens" type="cn" value=""/>
        <ParameterGroup name="Experiment Set">
        </ParameterGroup>
        <ParameterGroup name="Validation Set">
          <Parameter name="Weight" type="unsignedFloat" value="1"/>
          <Parameter name="Threshold" type="unsignedInteger" value="5"/>
        </ParameterGroup>
      </Problem>
      <Method name="Evolutionary Programming" type="EvolutionaryProgram">
        <Parameter name="Log Verbosity" type="unsignedInteger" value="0"/>
        <Parameter name="Number of Generations" type="unsignedInteger" value="200"/>
        <Parameter name="Population Size" type="unsignedInteger" value="20"/>
        <Parameter name="Random Number Generator" type="unsignedInteger" value="1"/>
        <Parameter name="Seed" type="unsignedInteger" value="0"/>
        <Parameter name="Stop after # Stalled Generations" type="unsignedInteger" value="0"/>
      </Method>
    </Task>
    <Task key="Task_7" name="Metabolic Control Analysis" type="metabolicControlAnalysis" scheduled="false" updateModel="false">
      <Report reference="Report_6" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="Steady-State" type="key" value="Task_13"/>
      </Problem>
      <Method name="MCA Method (Reder)" type="MCAMethod(Reder)">
        <Parameter name="Modulation Factor" type="unsignedFloat" value="1.0000000000000001e-09"/>
        <Parameter name="Use Reder" type="bool" value="1"/>
        <Parameter name="Use Smallbone" type="bool" value="1"/>
      </Method>
    </Task>
    <Task key="Task_6" name="Lyapunov Exponents" type="lyapunovExponents" scheduled="false" updateModel="false">
      <Report reference="Report_5" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="ExponentNumber" type="unsignedInteger" value="3"/>
        <Parameter name="DivergenceRequested" type="bool" value="1"/>
        <Parameter name="TransientTime" type="float" value="0"/>
      </Problem>
      <Method name="Wolf Method" type="WolfMethod">
        <Parameter name="Orthonormalization Interval" type="unsignedFloat" value="1"/>
        <Parameter name="Overall time" type="unsignedFloat" value="1000"/>
        <Parameter name="Relative Tolerance" type="unsignedFloat" value="9.9999999999999995e-07"/>
        <Parameter name="Absolute Tolerance" type="unsignedFloat" value="9.9999999999999998e-13"/>
        <Parameter name="Max Internal Steps" type="unsignedInteger" value="10000"/>
      </Method>
    </Task>
    <Task key="Task_5" name="Time Scale Separation Analysis" type="timeScaleSeparationAnalysis" scheduled="false" updateModel="false">
      <Report reference="Report_4" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="StepNumber" type="unsignedInteger" value="100"/>
        <Parameter name="StepSize" type="float" value="0.01"/>
        <Parameter name="Duration" type="float" value="1"/>
        <Parameter name="TimeSeriesRequested" type="bool" value="1"/>
        <Parameter name="OutputStartTime" type="float" value="0"/>
      </Problem>
      <Method name="ILDM (LSODA,Deuflhard)" type="TimeScaleSeparation(ILDM,Deuflhard)">
        <Parameter name="Deuflhard Tolerance" type="unsignedFloat" value="9.9999999999999995e-07"/>
      </Method>
    </Task>
    <Task key="Task_16" name="Sensitivities" type="sensitivities" scheduled="false" updateModel="false">
      <Report reference="Report_3" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="SubtaskType" type="unsignedInteger" value="1"/>
        <ParameterGroup name="TargetFunctions">
          <Parameter name="SingleObject" type="cn" value=""/>
          <Parameter name="ObjectListType" type="unsignedInteger" value="7"/>
        </ParameterGroup>
        <ParameterGroup name="ListOfVariables">
          <ParameterGroup name="Variables">
            <Parameter name="SingleObject" type="cn" value=""/>
            <Parameter name="ObjectListType" type="unsignedInteger" value="41"/>
          </ParameterGroup>
          <ParameterGroup name="Variables">
            <Parameter name="SingleObject" type="cn" value=""/>
            <Parameter name="ObjectListType" type="unsignedInteger" value="0"/>
          </ParameterGroup>
        </ParameterGroup>
      </Problem>
      <Method name="Sensitivities Method" type="SensitivitiesMethod">
        <Parameter name="Delta factor" type="unsignedFloat" value="0.001"/>
        <Parameter name="Delta minimum" type="unsignedFloat" value="9.9999999999999998e-13"/>
      </Method>
    </Task>
    <Task key="Task_15" name="Moieties" type="moieties" scheduled="false" updateModel="false">
      <Report reference="Report_0" target="" append="1" confirmOverwrite="1"/>
      <Problem>
      </Problem>
      <Method name="Householder Reduction" type="Householder">
      </Method>
    </Task>
    <Task key="Task_4" name="Cross Section" type="crosssection" scheduled="false" updateModel="false">
      <Problem>
        <Parameter name="AutomaticStepSize" type="bool" value="0"/>
        <Parameter name="StepNumber" type="unsignedInteger" value="100"/>
        <Parameter name="StepSize" type="float" value="0.01"/>
        <Parameter name="Duration" type="float" value="1"/>
        <Parameter name="TimeSeriesRequested" type="bool" value="1"/>
        <Parameter name="OutputStartTime" type="float" value="0"/>
        <Parameter name="Output Event" type="bool" value="0"/>
        <Parameter name="Start in Steady State" type="bool" value="0"/>
        <Parameter name="Use Values" type="bool" value="0"/>
        <Parameter name="Values" type="string" value=""/>
        <Parameter name="LimitCrossings" type="bool" value="0"/>
        <Parameter name="NumCrossingsLimit" type="unsignedInteger" value="0"/>
        <Parameter name="LimitOutTime" type="bool" value="0"/>
        <Parameter name="LimitOutCrossings" type="bool" value="0"/>
        <Parameter name="PositiveDirection" type="bool" value="1"/>
        <Parameter name="NumOutCrossingsLimit" type="unsignedInteger" value="0"/>
        <Parameter name="LimitUntilConvergence" type="bool" value="0"/>
        <Parameter name="ConvergenceTolerance" type="float" value="0"/>
        <Parameter name="Threshold" type="float" value="0"/>
        <Parameter name="DelayOutputUntilConvergence" type="bool" value="0"/>
        <Parameter name="OutputConvergenceTolerance" type="float" value="0"/>
        <ParameterText name="TriggerExpression" type="expression">
          
        </ParameterText>
        <Parameter name="SingleVariable" type="cn" value=""/>
        <Parameter name="Continue on Simultaneous Events" type="bool" value="0"/>
      </Problem>
      <Method name="Deterministic (LSODA)" type="Deterministic(LSODA)">
        <Parameter name="Integrate Reduced Model" type="bool" value="0"/>
        <Parameter name="Relative Tolerance" type="unsignedFloat" value="9.9999999999999995e-07"/>
        <Parameter name="Absolute Tolerance" type="unsignedFloat" value="9.9999999999999998e-13"/>
        <Parameter name="Max Internal Steps" type="unsignedInteger" value="10000"/>
        <Parameter name="Max Internal Step Size" type="unsignedFloat" value="0"/>
      </Method>
    </Task>
    <Task key="Task_3" name="Linear Noise Approximation" type="linearNoiseApproximation" scheduled="false" updateModel="false">
      <Report reference="Report_2" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="Steady-State" type="key" value="Task_13"/>
      </Problem>
      <Method name="Linear Noise Approximation" type="LinearNoiseApproximation">
      </Method>
    </Task>
    <Task key="Task_2" name="Time-Course Sensitivities" type="timeSensitivities" scheduled="false" updateModel="false">
      <Problem>
        <Parameter name="AutomaticStepSize" type="bool" value="0"/>
        <Parameter name="StepNumber" type="unsignedInteger" value="100"/>
        <Parameter name="StepSize" type="float" value="0.01"/>
        <Parameter name="Duration" type="float" value="1"/>
        <Parameter name="TimeSeriesRequested" type="bool" value="1"/>
        <Parameter name="OutputStartTime" type="float" value="0"/>
        <Parameter name="Output Event" type="bool" value="0"/>
        <Parameter name="Start in Steady State" type="bool" value="0"/>
        <Parameter name="Use Values" type="bool" value="0"/>
        <Parameter name="Values" type="string" value=""/>
        <ParameterGroup name="ListOfParameters">
        </ParameterGroup>
        <ParameterGroup name="ListOfTargets">
        </ParameterGroup>
      </Problem>
      <Method name="LSODA Sensitivities" type="Sensitivities(LSODA)">
        <Parameter name="Integrate Reduced Model" type="bool" value="0"/>
        <Parameter name="Relative Tolerance" type="unsignedFloat" value="9.9999999999999995e-07"/>
        <Parameter name="Absolute Tolerance" type="unsignedFloat" value="9.9999999999999998e-13"/>
        <Parameter name="Max Internal Steps" type="unsignedInteger" value="10000"/>
        <Parameter name="Max Internal Step Size" type="unsignedFloat" value="0"/>
      </Method>
    </Task>
  </ListOfTasks>
  <ListOfReports>
    <Report key="Report_10" name="Steady-State" taskType="steadyState" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Footer>
        <Object cn="CN=Root,Vector=TaskList[Steady-State]"/>
      </Footer>
    </Report>
    <Report key="Report_9" name="Elementary Flux Modes" taskType="fluxMode" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Footer>
        <Object cn="CN=Root,Vector=TaskList[Elementary Flux Modes],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_8" name="Optimization" taskType="optimization" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Optimization],Object=Description"/>
        <Object cn="String=\[Function Evaluations\]"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="String=\[Best Value\]"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="String=\[Best Parameters\]"/>
      </Header>
      <Body>
        <Object cn="CN=Root,Vector=TaskList[Optimization],Problem=Optimization,Reference=Function Evaluations"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="CN=Root,Vector=TaskList[Optimization],Problem=Optimization,Reference=Best Value"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="CN=Root,Vector=TaskList[Optimization],Problem=Optimization,Reference=Best Parameters"/>
      </Body>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Optimization],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_7" name="Parameter Estimation" taskType="parameterFitting" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Parameter Estimation],Object=Description"/>
        <Object cn="String=\[Function Evaluations\]"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="String=\[Best Value\]"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="String=\[Best Parameters\]"/>
      </Header>
      <Body>
        <Object cn="CN=Root,Vector=TaskList[Parameter Estimation],Problem=Parameter Estimation,Reference=Function Evaluations"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="CN=Root,Vector=TaskList[Parameter Estimation],Problem=Parameter Estimation,Reference=Best Value"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="CN=Root,Vector=TaskList[Parameter Estimation],Problem=Parameter Estimation,Reference=Best Parameters"/>
      </Body>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Parameter Estimation],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_6" name="Metabolic Control Analysis" taskType="metabolicControlAnalysis" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Metabolic Control Analysis],Object=Description"/>
      </Header>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Metabolic Control Analysis],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_5" name="Lyapunov Exponents" taskType="lyapunovExponents" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Lyapunov Exponents],Object=Description"/>
      </Header>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Lyapunov Exponents],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_4" name="Time Scale Separation Analysis" taskType="timeScaleSeparationAnalysis" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Time Scale Separation Analysis],Object=Description"/>
      </Header>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Time Scale Separation Analysis],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_3" name="Sensitivities" taskType="sensitivities" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Sensitivities],Object=Description"/>
      </Header>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Sensitivities],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_2" name="Linear Noise Approximation" taskType="linearNoiseApproximation" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Linear Noise Approximation],Object=Description"/>
      </Header>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Linear Noise Approximation],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_1" name="Time-Course" taskType="timeCourse" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Time-Course],Object=Description"/>
      </Header>
      <Footer>
        <Object cn="CN=Root,Vector=TaskList[Time-Course],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_0" name="Moieties" taskType="moieties" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Moieties],Object=Description"/>
      </Header>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Moieties],Object=Result"/>
      </Footer>
    </Report>
  </ListOfReports>
  <ListOfPlots>
    <PlotSpecification name="plot" type="Plot2D" active="1" taskTypes="">
      <Parameter name="log X" type="bool" value="0"/>
      <Parameter name="log Y" type="bool" value="0"/>
      <Parameter name="x axis" type="string" value=""/>
      <Parameter name="y axis" type="string" value=""/>
      <Parameter name="z axis" type="string" value=""/>
      <Parameter name="plot engine" type="string" value="QWT"/>
      <ListOfPlotItems>
        <PlotItem name="[R-TfEn]|Values[pH]" type="Curve2D">
          <Parameter name="Line type" type="unsignedInteger" value="2"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Vector=Values[pH],Reference=Value"/>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[R-TfEn],Reference=Concentration"/>
          </ListOfChannels>
        </PlotItem>
        <PlotItem name="[R-TfaEn]|Values[pH]" type="Curve2D">
          <Parameter name="Line type" type="unsignedInteger" value="2"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Vector=Values[pH],Reference=Value"/>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[R-TfaEn],Reference=Concentration"/>
          </ListOfChannels>
        </PlotItem>
        <PlotItem name="[TfaEn]|Values[pH]" type="Curve2D">
          <Parameter name="Line type" type="unsignedInteger" value="2"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Vector=Values[pH],Reference=Value"/>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[TfaEn],Reference=Concentration"/>
          </ListOfChannels>
        </PlotItem>
      </ListOfPlotItems>
    </PlotSpecification>
    <PlotSpecification name="Concentrations, Volumes, and Global Quantity Values" type="Plot2D" active="1" taskTypes="">
      <Parameter name="log X" type="bool" value="0"/>
      <Parameter name="log Y" type="bool" value="0"/>
      <Parameter name="plot engine" type="string" value="QCustomPlot"/>
      <ListOfPlotItems>
        <PlotItem name="[protonEn]" type="Curve2D">
          <Parameter name="Line type" type="unsignedInteger" value="0"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1.2"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Reference=Time"/>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[protonEn],Reference=Concentration"/>
          </ListOfChannels>
        </PlotItem>
        <PlotItem name="[R-TfaEn]" type="Curve2D">
          <Parameter name="Line type" type="unsignedInteger" value="0"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1.2"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Reference=Time"/>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[R-TfaEn],Reference=Concentration"/>
          </ListOfChannels>
        </PlotItem>
        <PlotItem name="[REn]" type="Curve2D">
          <Parameter name="Line type" type="unsignedInteger" value="0"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1.2"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Reference=Time"/>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[REn],Reference=Concentration"/>
          </ListOfChannels>
        </PlotItem>
        <PlotItem name="[TfaEn]" type="Curve2D">
          <Parameter name="Line type" type="unsignedInteger" value="0"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1.2"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Reference=Time"/>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[TfaEn],Reference=Concentration"/>
          </ListOfChannels>
        </PlotItem>
        <PlotItem name="[R-TfEn]" type="Curve2D">
          <Parameter name="Line type" type="unsignedInteger" value="0"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1.2"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Reference=Time"/>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[membrane],Vector=Metabolites[R-TfEn],Reference=Concentration"/>
          </ListOfChannels>
        </PlotItem>
        <PlotItem name="[FeEn]" type="Curve2D">
          <Parameter name="Line type" type="unsignedInteger" value="0"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1.2"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Reference=Time"/>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[FeEn],Reference=Concentration"/>
          </ListOfChannels>
        </PlotItem>
        <PlotItem name="[TfEn]" type="Curve2D">
          <Parameter name="Line type" type="unsignedInteger" value="0"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1.2"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Reference=Time"/>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[TfEn],Reference=Concentration"/>
          </ListOfChannels>
        </PlotItem>
        <PlotItem name="Values[pH]" type="Curve2D">
          <Parameter name="Line type" type="unsignedInteger" value="0"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1.2"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Reference=Time"/>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Vector=Values[pH],Reference=Value"/>
          </ListOfChannels>
        </PlotItem>
        <PlotItem name="Values[kfreeFe]" type="Curve2D">
          <Parameter name="Line type" type="unsignedInteger" value="0"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1.2"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Reference=Time"/>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Vector=Values[kfreeFe],Reference=Value"/>
          </ListOfChannels>
        </PlotItem>
        <PlotItem name="Values[kfreeTfa]" type="Curve2D">
          <Parameter name="Line type" type="unsignedInteger" value="0"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1.2"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Reference=Time"/>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Vector=Values[kfreeTfa],Reference=Value"/>
          </ListOfChannels>
        </PlotItem>
        <PlotItem name="Values[kdisTf]" type="Curve2D">
          <Parameter name="Line type" type="unsignedInteger" value="0"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1.2"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Reference=Time"/>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Vector=Values[kdisTf],Reference=Value"/>
          </ListOfChannels>
        </PlotItem>
        <PlotItem name="Values[kBindTf]" type="Curve2D">
          <Parameter name="Line type" type="unsignedInteger" value="0"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1.2"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Reference=Time"/>
            <ChannelSpec cn="CN=Root,Model=Plasma Membrane,Vector=Values[kBindTf],Reference=Value"/>
          </ListOfChannels>
        </PlotItem>
      </ListOfPlotItems>
    </PlotSpecification>
  </ListOfPlots>
  <GUI>
    <ListOfSliders>
      <Slider key="Slider_0" associatedEntityKey="Task_11" objectCN="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[protonEn],Reference=InitialConcentration" objectType="float" objectValue="3.98e-05" minValue="3.98e-05" maxValue="0.1" tickNumber="1000" tickFactor="100" scaling="linear"/>
      <Slider key="Slider_1" associatedEntityKey="Task_12" objectCN="CN=Root,Model=Plasma Membrane,Vector=Compartments[soluble],Vector=Metabolites[protonEn],Reference=InitialConcentration" objectType="float" objectValue="3.98e-05" minValue="3.98e-05" maxValue="0.1" tickNumber="1000" tickFactor="100" scaling="linear"/>
    </ListOfSliders>
  </GUI>
  <ListOfUnitDefinitions>
    <UnitDefinition key="Unit_1" name="meter" symbol="m">
      <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#Unit_0">
</rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        m
      </Expression>
    </UnitDefinition>
    <UnitDefinition key="Unit_5" name="second" symbol="s">
      <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#Unit_4">
</rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        s
      </Expression>
    </UnitDefinition>
    <UnitDefinition key="Unit_13" name="Avogadro" symbol="Avogadro">
      <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#Unit_12">
</rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        Avogadro
      </Expression>
    </UnitDefinition>
    <UnitDefinition key="Unit_17" name="item" symbol="#">
      <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#Unit_16">
</rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        #
      </Expression>
    </UnitDefinition>
    <UnitDefinition key="Unit_35" name="liter" symbol="l">
      <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#Unit_34">
</rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        0.001*m^3
      </Expression>
    </UnitDefinition>
    <UnitDefinition key="Unit_41" name="mole" symbol="mol">
      <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#Unit_40">
</rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        Avogadro*#
      </Expression>
    </UnitDefinition>
  </ListOfUnitDefinitions>
</COPASI>
