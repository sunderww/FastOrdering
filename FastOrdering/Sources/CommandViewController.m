//
//  CommandViewController.m
//  FastOrdering
//
//  Created by Sunder on 04/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "CommandViewController.h"
#import "CommandMenuViewController.h"
#import "Order+Custom.h"
#import "NSManagedObject+create.h"
#import "SocketHelper.h"

@interface CommandViewController ()

@end

@implementation CommandViewController

- (void)viewDidLoad {
  [super viewDidLoad];
  
  order = [Order create];
  menuModel = [OrderMenuModel new];
  carteModel = [OrderALaCarteModel new];
  reviewModel = [OrderReviewModel new];
  menuModel.delegate = self;
  carteModel.delegate = self;
//  reviewModel.delegate = self;
  reviewModel.order = order;
  reviewModel.tableView = reviewTableView;
  menuTableView.dataSource = menuModel;
  menuTableView.delegate = menuModel;
  carteTableView.dataSource = carteModel;
  carteTableView.delegate = carteModel;
  reviewTableView.delegate = reviewModel;
  reviewTableView.dataSource = reviewModel;
  
  [menuButton setTitle:NSLocalizedString(@"Menus", @"").uppercaseString forState:UIControlStateNormal];
  [alacarteButton setTitle:NSLocalizedString(@"A la carte", @"").uppercaseString forState:UIControlStateNormal];
  [reviewButton setTitle:NSLocalizedString(@"Order", @"").uppercaseString forState:UIControlStateNormal];
  
  [self buttonClicked:menuButton];
}

- (void)didReceiveMemoryWarning {
  [super didReceiveMemoryWarning];
  // Dispose of any resources that can be recreated.
}

#pragma mark - Menu and ALaCarte delegate methods

- (void)menuCompositionClicked:(MenuComposition *)composition {
  CommandMenuViewController * controller = [[CommandMenuViewController alloc] initWithNibName:@"CommandMenuView" bundle:nil];

  controller.composition = composition;
  controller.delegate = self;
  presentController = controller;

  CGRect frame = presentController.view.frame;
  frame.origin.x += frame.size.width;
  presentController.view.frame = frame;
  
  [self.view addSubview:presentController.view];
  [UIView animateWithDuration:0.5 animations:^{
    CGRect frame = presentController.view.frame;
    frame.origin.x -= frame.size.width;
    presentController.view.frame = frame;
  }];
}

- (void)dishClicked:(Dish *)dish {
  
}

#pragma mark - CommandMenuView delegate methods

- (void)didCreateOrderContent:(OrderContent *)content {
  [order addOrderContentsObject:content];
}

- (void)popCommandMenuView {
  [UIView animateWithDuration:0.5 animations:^{
    CGRect frame = presentController.view.frame;
    frame.origin.x += frame.size.width;
    presentController.view.frame = frame;
  } completion:^(BOOL finished) {
    [presentController.view removeFromSuperview];
    presentController = nil;
  }];
}

- (void)orderFailed {
  loaderView.hidden = YES;
  [[[UIAlertView alloc] initWithTitle:NSLocalizedString(@"error", @"") message:NSLocalizedString(@"Order_ErrorMessage", @"") delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil] show];
}

#pragma mark - IBAction methods

- (IBAction)buttonClicked:(UIButton *)sender {
  for (UIView * v in clickedViews)
    v.hidden = v.tag != sender.tag;
  for (UIView * v in contentViews)
    v.hidden = v.tag != sender.tag;
  [reviewModel reloadData];
}

- (IBAction)order {
  SocketHelper * helper = [SocketHelper sharedHelper];

  if (!helper.socket.isConnected) return [self orderFailed];

  loaderView.hidden = NO;
  [helper pushDelegate:self];
  [helper.socket sendEvent:@"send_order" withData:order.toJSON];
  timer = [NSTimer scheduledTimerWithTimeInterval:3.0 target:self selector:@selector(orderFailed) userInfo:nil repeats:NO];
}

#pragma mark - SocketIO delegate methods

- (void)socketIO:(SocketIO *)socket didReceiveMessage:(SocketIOPacket *)packet {
  PPLog(@"MESSAGE %@", packet);
}

- (void)socketIO:(SocketIO *)socket didReceiveJSON:(SocketIOPacket *)packet {
  PPLog(@"JSON %@", packet);
}

- (void)socketIO:(SocketIO *)socket didReceiveEvent:(SocketIOPacket *)packet {
  PPLog(@"EVENT %@", packet);
}

- (void)socketIO:(SocketIO *)socket didSendMessage:(SocketIOPacket *)packet {
  PPLog(@"SENT %@", packet);
}

- (void)socketIO:(SocketIO *)socket onError:(NSError *)error {
  [timer invalidate];
  [self orderFailed];
}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

@end
