package br.df.lseabra.rest.tests.refac.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import br.df.lseabra.rest.tests.refac.ContasTest;
import br.df.lseabra.rest.tests.refac.MovimentacaoTest;

@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({
	ContasTest.class,
	MovimentacaoTest.class
	
})
public class Suite {

}
