module HarborView.OptionPurchase.Command
  ( Action(..)
  , Field(..)
  ) where

--import Prelude
import Web.UIEvent.MouseEvent (MouseEvent)
import HarborView.OptionPurchase.Types (Purchase)

data Field
  = SellAmount
  | SellPrice

data Action
  = FetchPaper MouseEvent
  | FetchReal MouseEvent
  | SellDlgShow Purchase MouseEvent
  | SellDlgOk MouseEvent
  | SellDlgCancel MouseEvent
  | ValueChanged Field String
