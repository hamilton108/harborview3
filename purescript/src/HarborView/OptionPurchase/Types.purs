module HarborView.OptionPurchase.Types where

type Purchase =
  { oid :: Int
  , stock :: String
  , ot :: String -- option type
  , ticker :: String
  , pdate :: String -- purchase date
  , exp :: String
  , days :: Int
  , price :: Number
  , bid :: Number
  , spot :: Number
  , pvol :: Int -- purchase volume
  , svol :: Int -- sales volume
  }

type Purchases = Array Purchase