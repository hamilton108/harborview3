module HarborView.Common where

import Prelude

import Data.Either (Either(..))
import Effect (Effect)
import Effect.Class (liftEffect)
import Effect.Aff (Aff)
import Web.Event.Event as Event
-- import Effect.Console (logShow)

import Data.Number.Format as Format

foreign import alert :: String -> Effect Unit

newtype Amount =
  Amount Int

newtype Price =
  Price Number

newtype Oid =
  Oid Int

newtype Url =
  Url String

data HarborViewError
  = AffjaxError String
  | JsonError String

handleErrorAff :: HarborViewError -> Aff Unit
handleErrorAff (AffjaxError err) =
  liftEffect $ alert $ "AffjaxError: " <> err
handleErrorAff (JsonError err) =
  liftEffect $ alert $ "JsonError: " <> err

errToString :: HarborViewError -> String
errToString (AffjaxError err) =
  "AffjaxError: " <> err
errToString (JsonError err) =
  "JsonError: " <> err

numToString :: Number -> String
numToString num =
  Format.toStringWith (Format.fixed 2) num

type JsonResult =
  { oid :: Int
  , msg :: String
  , statusCode :: Int
  }

jsonResultToString :: Either HarborViewError JsonResult -> String
jsonResultToString result =
  case result of
    Left err ->
      errToString err
    Right result1 ->
      result1.msg

defaultEventHandling :: Event.Event -> Effect Unit
defaultEventHandling event =
  Event.stopPropagation event *>
    Event.preventDefault event

dayInMillis :: Number
dayInMillis = 86400000.0

------------------------- UnixTime ------------------------- 
newtype UnixTime = UnixTime Number

derive instance eqUnixTime :: Eq UnixTime

instance showUnixTime :: Show UnixTime where
  show (UnixTime v) = "(UnixTime " <> show v <> ")"

instance ordUnixTime :: Ord UnixTime where
  compare (UnixTime u1) (UnixTime u2) = compare u1 u2
