module HarborView.Maunaloa.LevelLine 
  ( addLine
  , deleteAll 
  , fetchLevelLines 
  , initEvents
  , Line(..)
  , updateVruler
  ) where

import Prelude
import Data.Maybe (Maybe(..))
--import Data.Array ((:)) 
import Data.Either (Either(..))
import Data.Number.Format (toStringWith,fixed)
import Effect (Effect)
import Effect.Class (liftEffect)
import Effect.Console (logShow)
import Effect.Aff (Aff, launchAff_)

import Affjax.Web as Affjax
import Affjax.ResponseFormat as ResponseFormat
import Data.Traversable as Traversable

import Graphics.Canvas as Canvas 
import Graphics.Canvas (CanvasElement,Context2D)
import Web.Event.Event (EventType(..))
import Web.Event.Event as Event
import Web.Event.EventTarget as EventTarget
--import Effect.Ref as Ref
import Web.DOM.NonElementParentNode (NonElementParentNode,getElementById)
import Web.DOM.Element (toEventTarget,Element)
import Web.HTML as HTML
import Web.HTML.Window as Window
import Web.HTML.HTMLDocument as HTMLDocument

import Data.Argonaut.Core (Json)
import Data.Argonaut.Decode as Decode
import Data.Argonaut.Decode.Error (JsonDecodeError)
import HarborView.Maunaloa.MaunaloaError
  ( MaunaloaError(..)
  , handleErrorAff
  )


import HarborView.Common (defaultEventHandling)
import HarborView.Maunaloa.Common 
  ( Pix(..)
  , ChartMapping(..)
  , HtmlId(..)
  , OptionTicker(..)
  , StockTicker(..)
  , ChartType
  , chartTypeAsInt
  , mainURL
  , alert)
import HarborView.Maunaloa.VRuler (VRuler,valueToPix,pixToValue)

{-
import Data.IORef (newIORef,modifyIORef,readIORef)

type Counter = Int -> IO Int

makeCounter :: IO Counter
makeCounter = do
    r <- newIORef 0
    return (\i -> do modifyIORef r (\q -> q + i) -- (+i)
                    readIORef r)

testCounter :: Counter -> IO ()
testCounter counter = do
  b <- counter 1
  c <- counter 1
  d <- counter 1
  print [b,c,d]

main = do
  counter <- makeCounter
  testCounter counter
  testCounter counter
-}

foreign import clearLines :: Int -> Effect Unit

foreign import addLineImpl :: Int -> Line -> Effect Unit

foreign import onMouseDown :: Int -> Event.Event -> Effect Unit

foreign import onMouseDrag :: Int -> Event.Event -> Effect Unit

foreign import onMouseUpImpl :: Int -> (Line -> Maybe Line) -> (Maybe Line) -> Effect (Maybe Line)

foreign import updateRiscLine :: Int -> Line -> Number -> Effect Unit

foreign import updateVrulerImpl :: Int -> VRuler -> Effect Unit 

foreign import updateCtxImpl :: Int -> Context2D -> Effect Unit 

foreign import randomRgb :: Effect String

foreign import currentVruler :: Int -> Effect VRuler

--foreign import showJson :: Json -> Effect Unit

--foreign import alert :: String -> Effect Unit


data Line = 
  StdLine 
  { y :: Number
  , selected :: Boolean
  , lt :: Int
  , color :: String
  } 
  | RiscLine
  { y :: Number
  , selected :: Boolean
  , ticker :: OptionTicker 
  , ask :: Number
  , bid :: Number
  , risc :: Number
  , riscPrice :: Number
  , lt :: Int
  }
  | BreakEvenLine
  { y :: Number
  , ticker :: OptionTicker
  , ask :: Number
  , breakEven :: Number
  , lt :: Int
  }

instance showLine :: Show Line where
  show (StdLine v) = "StdLine: " <> show v 
  show (RiscLine v) = "RiscLine: " <> show v 
  show (BreakEvenLine v) = "BreakEvenLine: " <> show v 

ltSTD :: Int
ltSTD= 1

ltRISC :: Int
ltRISC = 2

ltBREAK_EVEN :: Int 
ltBREAK_EVEN= 3

onMouseUp :: ChartType -> Event.Event -> Effect (Maybe Line)
onMouseUp ct _ = onMouseUpImpl (chartTypeAsInt ct) Just Nothing 

{-
const STD_LINE = 1;
const RISC_LINE = 2;
const BREAK_EVEN_LINE = 3;
-}

{-}
newtype PilotLine = 
    PilotLine 
    { y :: Number
    , strokeStyle :: String
    } 

derive instance eqPilotLine :: Eq PilotLine 

instance showPilotLine :: Show PilotLine where
    show (PilotLine v) = "PilotLine : " <> show v 

newtype Lines = 
    Lines
    { lines :: Array Line
    , pilotLine :: Maybe PilotLine 
    }

instance showLines :: Show Lines where
    show (Lines { lines, pilotLine }) = "Lines, " <> show lines <> ", pilotLine: " <> show pilotLine
-}

getDoc :: Effect NonElementParentNode
getDoc = 
  HTML.window >>= \win ->
    Window.document win >>= \doc ->
      pure $ HTMLDocument.toNonElementParentNode doc

newtype EventListenerInfo =
  EventListenerInfo 
  { target :: Element 
  , listener :: EventTarget.EventListener
  , eventType :: EventType
  }

-- type EventListeners = Array EventListenerInfo -- List.List EventListenerInfo 

-- type EventListenerRef = Ref.Ref EventListeners

type HtmlContext = 
  { canvasContext :: CanvasElement --Canvas.Context2D
  , canvasElement :: Element
  }

type RiscLineJson = 
  { ticker :: String
  , be :: Number
  , riscStockPrice :: Number
  , riscOptionPrice :: Number
  , bid :: Number
  , ask :: Number
  , risc :: Number
  }

type RiscLinesJson = Array RiscLineJson

riscLinesFromJson :: Json -> Either JsonDecodeError RiscLinesJson 
riscLinesFromJson = Decode.decodeJson

type UpdatedOptionPriceJson = 
  { value :: Number
  }

updOptionPriceFromJson :: Json -> Either JsonDecodeError UpdatedOptionPriceJson 
updOptionPriceFromJson = Decode.decodeJson

-- unlisten :: EventListenerInfo -> Effect Unit
-- unlisten (EventListenerInfo {target,listener,eventType}) = 
--   EventTarget.removeEventListener eventType listener false (toEventTarget target)

-- unlistenEvents :: ChartType -> Effect Unit
-- unlistenEvents ct = 
--   let 
--     cti = chartTypeAsInt ct
--   in
--   getListeners cti >>= \listeners ->
--   Traversable.traverse_ unlisten listeners *>
--   resetListeners cti

{-
unlistener :: EventListenerRef -> Int -> Effect Unit
unlistener elr dummy =
    Ref.read elr >>= \elrx -> 
        Traversable.traverse_ unlisten elrx

remButtonClick :: Event -> Effect Unit
remButtonClick evt =
    getListeners >>= \listeners ->
    Traversable.traverse_ unlisten listeners *>
    resetListeners 
-}

deleteAll :: ChartType -> Effect Unit
deleteAll ct =
  clearLines (chartTypeAsInt ct)

addLine :: ChartType -> Effect Unit
addLine ct =
  randomRgb >>= \curColor ->
  let
    line = StdLine { y: 200.0, selected: false, lt: ltSTD, color: curColor }
  in
  addLineImpl (chartTypeAsInt ct) line

fetchLevelLinesURL :: StockTicker -> String
fetchLevelLinesURL (StockTicker ticker) =
    -- "http://localhost:6346/maunaloa/risclines/" <> ticker
  mainURL <>  "/risclines/" <> ticker

optionPriceURL :: OptionTicker -> Number -> String
optionPriceURL (OptionTicker ticker) curStockPrice =
  mainURL <> "/stockoption/price/" <> ticker <> "/" <> toStringWith (fixed 2) curStockPrice


addRiscLine :: ChartType -> RiscLineJson -> Effect Unit
addRiscLine ct line = 
  let 
    cti = chartTypeAsInt ct
    ticker = OptionTicker line.ticker
  in
  currentVruler cti >>= \vr ->
    let 
      rl = 
        RiscLine
        { y: valueToPix vr line.riscStockPrice
        , selected: false
        , ticker: ticker
        , ask: line.ask 
        , bid: line.bid
        , risc: line.risc 
        , riscPrice: line.riscOptionPrice
        , lt: ltRISC
        }
      bl = 
        BreakEvenLine
        { y: valueToPix vr line.be
        , ticker: ticker
        , ask: line.ask 
        , breakEven: line.be
        , lt: ltBREAK_EVEN
        }
    in
    addLineImpl cti rl *>
    addLineImpl cti bl 

addRiscLines :: ChartType -> RiscLinesJson -> Effect Unit
addRiscLines ct lines = 
  let 
    addRiscLine1 = addRiscLine ct
  in
  Traversable.traverse_ addRiscLine1 lines

fetchLevelLines_ :: StockTicker -> Aff (Either MaunaloaError RiscLinesJson)
fetchLevelLines_ ticker = 
  Affjax.get ResponseFormat.json (fetchLevelLinesURL ticker) >>= \res ->
    let 
      result :: Either MaunaloaError RiscLinesJson 
      result = 
        case res of  
          Left err -> 
            Left $ AffjaxError (Affjax.printError err)
          Right response ->
            let 
              lines = riscLinesFromJson response.body
            in
            case lines of
              Left err ->
                Left $ JsonError (show err)
              Right lines1 ->
                Right lines1 
    in
    pure result

fetchLevelLines :: ChartType -> StockTicker -> Effect Unit
fetchLevelLines ct ticker = 
  -- defaultEventHandling evt *>
  launchAff_ 
  (
    fetchLevelLines_ ticker >>= \lines ->
      case lines of
        Left err ->
          handleErrorAff err
        Right lines1 ->
          liftEffect 
          (
            clearLines (chartTypeAsInt ct) *>
            addRiscLines ct lines1
          )
  )

mouseEventDown :: ChartType -> Event.Event -> Effect Unit
mouseEventDown ct evt = 
  defaultEventHandling evt *>
  onMouseDown (chartTypeAsInt ct) evt 

mouseEventDrag :: ChartType -> Event.Event -> Effect Unit
mouseEventDrag ct evt = 
  defaultEventHandling evt *>
  onMouseDrag (chartTypeAsInt ct) evt

fetchUpdatedOptionPrice :: OptionTicker -> Number -> Aff (Either MaunaloaError Number)
fetchUpdatedOptionPrice ticker curStockPrice = 
  Affjax.get ResponseFormat.json (optionPriceURL ticker curStockPrice) >>= \res ->
    let 
      result :: Either MaunaloaError Number 
      result = 
        case res of  
          Left err -> 
            Left $ AffjaxError (Affjax.printError err)
          Right response -> 
            let 
              json = updOptionPriceFromJson response.body 
            in
            case json of
              Left err ->
                Left $ JsonError (show err)
              Right json1 ->
                Right json1.value
    in 
    pure result

handleUpdateOptionPrice :: ChartType -> VRuler -> Line -> Effect Unit
handleUpdateOptionPrice ct vr lref@(RiscLine line) = 
  launchAff_ $
    let 
      cti = chartTypeAsInt ct
      sp = pixToValue vr (Pix line.y)
    in
    fetchUpdatedOptionPrice line.ticker sp >>= \n ->
      case n of 
        Left err ->
          handleErrorAff err
        Right value ->
          (liftEffect $ updateRiscLine cti lref value)
handleUpdateOptionPrice _ _ _ = 
  pure unit

handleMouseEventUpLine :: ChartType -> VRuler -> Maybe Line -> Effect Unit
handleMouseEventUpLine ct vr line = 
  case line of 
    Nothing -> 
      pure unit
    Just line1 ->
      handleUpdateOptionPrice ct vr line1

{- 
        Just lref@(RiscLine rec0) ->76
            logShow rec0 *>
            let 
                oldOpPrice = rec0.bid
                newPrice = oldOpPrice * 1.2
            in
            updateRiscLine lref newPrice 
        _ -> 
            pure unit
-}

mouseEventUp :: ChartType -> Event.Event -> Effect Unit
mouseEventUp ct evt = 
  defaultEventHandling evt *>
  onMouseUp ct evt >>= \line ->
  currentVruler (chartTypeAsInt ct) >>= \vruler ->
  handleMouseEventUpLine ct vruler line 


getHtmlContext1 :: 
  { canvas :: Maybe Element
  , ctx :: Maybe CanvasElement } -> Maybe HtmlContext
getHtmlContext1 prm = 
  prm.canvas >>= \canvas1 ->
  prm.ctx >>= \ctx1 ->
    Just
    { canvasContext: ctx1 
    , canvasElement: canvas1 
    }

validateMaybe :: forall a . String -> Maybe a -> Effect Unit
validateMaybe desc el = 
  case el of
    Nothing -> alert ("ERROR!: " <> desc)
    Just _ -> pure unit -- logShow ("OK: " <> desc)

getHtmlContext :: forall r. 
  { levelCanvasId :: HtmlId | r } 
  -> Effect (Maybe HtmlContext)
getHtmlContext 
  { levelCanvasId : (HtmlId levelCanvasId1) 
  } = 
  logShow ("levelCanvasId1: " <> levelCanvasId1) *>
  getDoc >>= \doc ->
    getElementById levelCanvasId1 doc >>= \canvasElement ->
    Canvas.getCanvasElementById levelCanvasId1 >>= \canvas ->
    validateMaybe "canvasElement" canvasElement *>
    validateMaybe "canvas" canvas *>
    pure (getHtmlContext1 { canvas: canvasElement
                          , ctx: canvas })
getHtmlContext _ = 
  pure Nothing

initEvent :: (Event.Event -> Effect Unit) -> Element -> EventType -> Effect Unit
initEvent toListener element eventType =
  EventTarget.eventListener toListener >>= \e1 -> 
    EventTarget.addEventListener eventType e1 false (toEventTarget element) 

initEvents :: ChartType -> ChartMapping -> Effect Unit
initEvents ct (ChartMapping cm) =
  getHtmlContext cm >>= \context ->
    case context of
        Nothing ->
          logShow ("(initEvents) No getHtmlContext for: " <> (show cm.levelCanvasId)) *>
          pure unit
        Just context1 ->
          let 
            ce = context1.canvasContext  
          in
          Canvas.getContext2D ce >>= \ctx ->
            updateCtxImpl (chartTypeAsInt ct) ctx *>
            initEvent (mouseEventDown ct) context1.canvasElement (EventType "mousedown") *>
            initEvent (mouseEventDrag ct) context1.canvasElement (EventType "mousemove") *>
            initEvent (mouseEventUp ct) context1.canvasElement (EventType "mouseup") 

updateVruler :: ChartType -> VRuler -> Effect Unit
updateVruler ct vr = 
  updateVrulerImpl (chartTypeAsInt ct) vr
