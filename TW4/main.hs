import           Data.List.Split
import           Data.Typeable
import           System.Environment
import           System.IO
import           Data.List
import           Control.Lens
import           Data.Char
import           Data.String

data Transaction = Transaction {
        symbol     :: String,
        variable   :: String,
        production :: [String]
    } deriving (Show)

data FoatStack = FoatStack {
      stack       :: [String],
      var         :: String
}

type Foat = [String]

type Pair = (String, String)

-- parsing:

splitFileContent :: String -> [String]
splitFileContent = \x -> splitOn "\n" x

makeTransactionFromLines :: [String] -> [Transaction]
makeTransactionFromLines [] = []
makeTransactionFromLines (x:xs) = do
    let splitted = splitOn "=" x
    let (x,y) = parseVariablesAndSymbol (splitted  ^? element 0)
    (makeTransaction x y splitted) ++ makeTransactionFromLines xs

makeTransaction :: String -> String -> [String] -> [Transaction]
makeTransaction [] x l = []
makeTransaction x [] l = []
makeTransaction x y z = [Transaction x y (parseProduction (z  ^? element 1))]

parseVariablesAndSymbol :: Maybe String -> Pair
parseVariablesAndSymbol x = case x of
                            Nothing -> ("", "")
                            Just y -> ((splitted !! 0), (splitted !! 2))
                                where splitted = splitOn " " y

parseProduction :: Maybe String -> [String]
parseProduction x = case x of
                    Nothing -> []
                    Just y -> filter (\z -> isValidProduction z) (splitOn "" y)

isValidProduction :: String -> Bool
isValidProduction [x] = isLetter x
isValidProduction _ = False

-- D:
dependencyClass :: [Transaction] -> [Pair]
dependencyClass x = [(symbol y, symbol z) | y <- x, z <- (dependent x y)]

dependent :: [Transaction] -> Transaction -> [Transaction]
dependent transactions current = [y | y <- transactions, (variable current) `elem` (production y) || (variable current) == (variable y)]

--I:
independetClass :: [Pair] -> [String] -> [Pair]
independetClass dp v = [(y,z) | y <- v, z <- v, not ((y,z) `elem` dp) && not ((z,y) `elem` dp)]

--Foat
foatClass :: [Pair] -> [String] -> String -> [Foat]
foatClass dp alphabet word = convertStacksToFoat $ fillStacksToMaxSize (fillStacks word dp alphabetStacks)
                          where alphabetStacks = map (\x -> FoatStack [] x) alphabet

fillStacks :: String -> [Pair] -> [FoatStack] -> [FoatStack]
fillStacks [] dp stacks = stacks
fillStacks (x : xs) dp stacks = fillStacks xs dp (map (\c -> addToStacks c [x] dp) stacks)

addToStacks :: FoatStack -> String -> [Pair] -> FoatStack
addToStacks st v dp = if (isVariableDependent v dp)
                        then FoatStack (v : (stack st)) (var st)
                        else FoatStack ("*" : (stack st)) (var st)

isVariableDependent :: String -> [Pair] ->Bool
isVariableDependent v dp = ([] /= [k | (k,j) <- dp, k == v || j == v])

convertStacksToFoat :: [FoatStack] -> [Foat]
convertStacksToFoat x | (concat $ map (\c -> (stack c)) x) == [] = []
                      | otherwise = ((filterStars . joinTops) x) : (convertStacksToFoat $ sliceTops x)

fillStacksToMaxSize :: [FoatStack] -> [FoatStack]
fillStacksToMaxSize x = map (\s -> FoatStack (fillMissingLength s maxStackSize) (var s)) x
                                    where maxStackSize = getMaxStackSize x

fillMissingLength :: FoatStack -> Int -> [String]
fillMissingLength st maxLength = (stack st) ++ ["*" | _ <- [0 .. missing]]
                                    where missing = maxLength - (length (stack st)) 

getMaxStackSize :: [FoatStack] -> Int
getMaxStackSize x = maximum (map (\s -> length (stack s)) x)

joinTops :: [FoatStack] -> [String]
joinTops [] = []
joinTops (x : xs) = (head $ stack x) : (joinTops xs)

sliceTops :: [FoatStack] -> [FoatStack]
sliceTops [] = []
sliceTops (x : xs) = (FoatStack (tail $ stack x) (var x)) : (sliceTops xs)

filterStars :: [String] -> [String]
filterStars x = filter (\s -> s == "*") x

-- Util:
prettifyTuplesList :: [Pair] -> String
prettifyTuplesList x = "{" ++ (intercalate " , " (map (\s -> printTuple s) x)) ++ "} + symetria"

printTuple :: Pair -> String
printTuple (x,y) = "(" ++ x ++ "," ++ y ++ ")"

main = do
    (inFileName:_) <- getArgs
    putStrLn inFileName
    handler <- openFile inFileName ReadMode
    fileContent <- hGetContents handler
    let lines = splitFileContent fileContent
    let content = makeTransactionFromLines (tail lines)
    let alphabet = map (\x -> symbol x) content
    let dependent = dependencyClass content
    putStrLn (prettifyTuplesList dependent)
    let independentR = independetClass dependent alphabet
    putStrLn (prettifyTuplesList independentR)
    putStrLn (show (foatClass dependent alphabet (head lines)))
    -- foatClass :: [Pair] -> [String] -> String -> [Foat]
