module Rapanui.Common
  ( Ask(..)
  , Bid(..)
  , Cid(..)
  , CritterType(..)
  , Iso8601(..)
  , Msg(..)
  , NordnetHost(..)
  , NordnetPort(..)
  , Oid(..)
  , OptionTicker(..)
  , Pid(..)
  , PosixTimeInt(..)
  , Rtyp(..)
  , Status(..)
  ) where

-- import Prelude

newtype OptionTicker = OptionTicker String

-- newtype StockTicker = StockTicker String deriving (Eq,Show,Generic)

newtype Oid = Oid Int

newtype Pid = Pid Int

newtype Cid = Cid Int

newtype Rtyp = Rtyp Int

newtype CritterType = CritterType String

newtype NordnetHost = NordnetHost String

newtype NordnetPort = NordnetPort Int

newtype Bid = Bid Number

newtype Ask = Ask Number

newtype Status = Status Int

newtype Msg = Msg String

newtype PosixTimeInt = PosixTimeInt Int

newtype Iso8601 = Iso8601 String

--newtype MarketOpen = MarketOpen TimeOfDay deriving (Show)

--newtype MarketClose = MarketClose TimeOfDay deriving (Show)