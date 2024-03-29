
import java.util.Collection;
import java.util.List;
import java.io.StringReader;
import java.lang.annotation.Annotation;

import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.semgraph.SemanticGraph;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

class ParserDemo {


  public static void main(String[] args) {
    LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
    if (args.length > 0) {
      demoDP(lp, args[0]);
    } else {
      demoAPI(lp);
    }
  }

  public static void demoDP(LexicalizedParser lp, String filename) {
    // This option shows loading and sentence-segment and tokenizing
    // a file using DocumentPreprocessor
    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
    // You could also create a tokenizer here (as below) and pass it
    // to DocumentPreprocessor
    for (List<HasWord> sentence : new DocumentPreprocessor(filename)) {
      Tree parse = lp.apply(sentence);
      parse.pennPrint();
      System.out.println();

      GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
      Collection tdl = gs.typedDependenciesCCprocessed(true);
      System.out.println(tdl);
      System.out.println();
    }
  }

  public static void demoAPI(LexicalizedParser lp) {


    // This option shows loading and using an explicit tokenizer
    String sent2 = "I agree with your argument.";
    TokenizerFactory<CoreLabel> tokenizerFactory =
      PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
    List<CoreLabel> rawWords2 =
      tokenizerFactory.getTokenizer(new StringReader(sent2)).tokenize();
    Tree parse = lp.apply(rawWords2);

    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
    List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
    System.out.println(tdl);
//    for(Tree t : parse.getLeaves())
    System.out.println(parse.taggedYield().get(0).tag());
    System.out.println(parse.getLeaves().get(0));
    System.out.println(parse.taggedYield().get(3).tag());
    System.out.println(parse.getLeaves().get(3));
    System.out.println(parse.yield());//pathNodeToNode(parse.getLeaves().get(0), parse.getLeaves().get(3)));
    
    
    
    
    TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
   // parse.pennPrint();
   // tp.printTree(parse);
  }

  private ParserDemo() {} // static methods only

}
