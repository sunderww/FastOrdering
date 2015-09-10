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
#import "AppDelegate.h"
#import "UNIRest.h"

@interface CommandViewController ()

@end

@implementation CommandViewController

- (void)viewDidLoad {
	[super viewDidLoad];
	
	if (self.order) {
		forceReview = YES;
	} else {
		self.order = [Order create];
		forceReview = NO;
	}

	menuModel = [OrderMenuModel new];
	carteModel = [[OrderALaCarteModel alloc] initWithOrder:self.order];
	reviewModel = [OrderReviewModel new];
	menuModel.delegate = self;
//  reviewModel.delegate = self;
	reviewModel.order = self.order;
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

	[self buttonClicked:(forceReview ? reviewButton : menuButton)];
}

- (void)viewWillDisappear:(BOOL)animated {
	[super viewWillDisappear:animated];

	AppDelegate * delegate = ((AppDelegate *)UIApplication.sharedApplication.delegate);
	if (!didOrder && !forceReview)
		[delegate.managedObjectContext deleteObject:self.order];

	[delegate saveContext];
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
	AppDelegate * delegate = ((AppDelegate *)UIApplication.sharedApplication.delegate);
	
	if (content.dishes.count > 0) {
		[self.order addOrderContentsObject:content];
	} else {
		[delegate.managedObjectContext deleteObject:content];
	}

	[delegate saveContext];
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

	if (sender.tag == alacarteView.tag) {
		[carteModel reloadData];
		[carteTableView reloadData];
		carteModel.editing = YES;
	} else {
		[self.order sanitize];
		carteModel.editing = NO;
	}
	[reviewModel reloadData];
}

- (IBAction)takeOrder {
	SocketHelper * helper = [SocketHelper sharedHelper];
	[self.order sanitize];
	self.order.numTable = @"2";
	PPLog(@"JSON :\n\n%@\n\n", self.order.toJSONString);

//	DPPLog(@"FORCE FAIL");
//	return [self orderFailed];

//	NSDictionary* headers = @{@"Content-Type": @"application/json"};
//	
//	UNIHTTPJsonResponse *response = [[UNIRest post:^(UNISimpleRequest *request) {
//  [request setUrl:[NSString stringWithFormat:@"http://%@:%d/send_order", kSocketIOHost, kSocketIOPort]];
//  [request setHeaders:headers];
//  [request setParameters:@{@"json": self.order.toJSONString}];
//	}] asJson];
//	
//	PPLog(@"%@", response.body);
	
	if (!helper.socket.isConnected) return [self orderFailed];
	
	loaderView.hidden = NO;
	[helper pushDelegate:self];
	[helper.socket sendEvent:@"send_order" withData:self.order.toJSON andAcknowledge:^(id argsData) {
		NSDictionary * dict = argsData;
		
		if (dict[@"error"]) {
			[timer fire];
		} else {
			[timer invalidate];
			loaderView.hidden = YES;
			didOrder = YES;
			
			self.order.createdAt = [NSDate date];
			self.order.updatedAt = [NSDate date];
			self.order.serverId = dict[@"numOrder"];
			[self.mainController goBackToMainPage];
		}
	}];

	// find a way to get a callback
	timer = [NSTimer scheduledTimerWithTimeInterval:5.0 target:self selector:@selector(orderFailed) userInfo:nil repeats:NO];
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
