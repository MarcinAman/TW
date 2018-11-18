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

parseVariablesAndSymbol :: Maybe String -> (String, String)
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
dependencyClass :: [Transaction] -> [(String, String)]
dependencyClass x = [(symbol y, symbol z) | y <- x, z <- (dependent x y)]

dependent :: [Transaction] -> Transaction -> [Transaction]
dependent transactions current = [y | y <- transactions, (variable current) `elem` (production y) || (variable current) == (variable y)]

--I:
independetClass :: [(String, String)] -> [String] -> [(String, String)]
independetClass dp v = [(y,z) | y <- v, z <- v, not ((y,z) `elem` dp) && not ((z,y) `elem` dp)]

-- Util:
prettifyTuplesList :: [(String, String)] -> String
prettifyTuplesList x = "{" ++ (intercalate " , " (map (\s -> printTuple s) x)) ++ "} + symetria"

printTuple :: (String, String) -> String
printTuple (x,y) = "(" ++ x ++ "," ++ y ++ ")"

main = do
    (inFileName:_) <- getArgs
    putStrLn inFileName
    handler <- openFile inFileName ReadMode
    fileContent <- hGetContents handler
    let content = (makeTransactionFromLines . splitFileContent) (fileContent)
    let alphabet = map (\x -> symbol x) content
    let dependent = dependencyClass content
    putStrLn (prettifyTuplesList dependent)
    let independentR = independetClass dependent alphabet
    putStrLn (prettifyTuplesList independentR)
