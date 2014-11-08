//
//  MainViewController.m
//  FastOrdering
//
//  Created by Sunder on 15/10/2014.
//  Copyright (c) 2014 lucas.bergognon. All rights reserved.
//

#import "MainViewController.h"
#import "NotificationViewController.h"
#import "TablePlanViewController.h"
#import "MenuViewController.h"
#import "SocketIO.h"
#import "AppDelegate.h"

@interface MainViewController ()

@end

@implementation MainViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    socket = [[SocketIO alloc] initWithDelegate:self];
    [socket connectToHost:kSocketIOHost onPort:kSocketIOPort];
    [socket sendEvent:@"test" withData:@{@"param":@"test", @"param2":@"test2"}];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - IBAction methods

- (IBAction)buttonClicked:(id)sender {
    UIViewController * nextController;
    
    if (sender == tableButton) {
        nextController = [[TablePlanViewController alloc] initWithNibName:@"TablePlanView" bundle:nil];
    } else if (sender == menuButton) {
        nextController = [[MenuViewController alloc] initWithNibName:@"MenuView" bundle:nil];
    } else if (sender == historyButton) {
        
    } else if (sender == notificationsButton) {
        nextController = [[NotificationViewController alloc] initWithNibName:@"NotificationView" bundle:nil];
    }
    
    [self.navigationController pushViewController:nextController animated:YES];
}

- (IBAction)logOut {
    [self.navigationController popViewControllerAnimated:YES];
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

- (void)socketIODidConnect:(SocketIO *)socket {
    PPLog(@"CONNECT");
}

- (void)socketIODidDisconnect:(SocketIO *)socket disconnectedWithError:(NSError *)error {
    PPLog(@"DECO %@", error);
}

- (void)socketIO:(SocketIO *)socket onError:(NSError *)error {
    PPLog(@"%@", error);
}


/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
